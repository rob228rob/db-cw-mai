package com.mai.db_cw_rbl.restoring;

import com.mai.db_cw_rbl.restoring.dto.DBProcessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class DBLifecycleService {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    private static final String BACKUP_PATH = "/opt/backups/";
    private static final String BACKUP_FILE = "db_backup.sql";

    private final ResourceLoader resourceLoader;

    /**
     * Метод для создания резервной копии текущей базы данных
     *
     * @return ответ о результате операции
     */
    public DBProcessResponse backupCurrentDB() {
        try {
            String dbName = getDatabaseNameFromUrl(dbUrl);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String backupFilePath = BACKUP_PATH + BACKUP_FILE + "_" + timestamp;

            ProcessBuilder pb = new ProcessBuilder(
                    "pg_dump",
                    "-h", "main_db", // Указание хоста базы данных
                    "-U", dbUser,
                    "-d", dbName,
                    "-f", backupFilePath
            );

            pb.environment().put("PGPASSWORD", dbPassword);
            pb.redirectErrorStream(true);

            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                log.info("pg_dump: {}", line);
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                // Получение размера файла резервной копии
                long fileSize = new File(backupFilePath).length();
                return DBProcessResponse.builder()
                        .msg("Резервное копирование выполнено успешно")
                        .success(true)
                        .bytes(fileSize)
                        .build();
            } else {
                return DBProcessResponse.builder()
                        .bytes(0)
                        .success(false)
                        .msg("Резервное копирование не удалось")
                        .build();
            }

        } catch (Exception e) {
            log.error("Ошибка при резервном копировании", e);
            return DBProcessResponse.builder()
                    .bytes(0)
                    .success(false)
                    .msg("Резервное копирование не удалось: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Метод для восстановления базы данных из последней резервной копии
     *
     * @return ответ о результате операции
     */
    public DBProcessResponse restoreCurrentDB() {
        try {
            String dbName = getDatabaseNameFromUrl(dbUrl);
            String backupFilePath = findLatestBackup();

            if (backupFilePath == null) {
                return DBProcessResponse.builder()
                        .bytes(0)
                        .success(false)
                        .msg("Файлы резервных копий не найдены")
                        .build();
            }

            ProcessBuilder pb = new ProcessBuilder(
                    "psql",
                    "-h", "main_db", // Указание хоста базы данных
                    "-U", dbUser,
                    "-d", dbName,
                    "-f", backupFilePath
            );

            pb.environment().put("PGPASSWORD", dbPassword);
            pb.redirectErrorStream(true);

            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                log.info("psql: {}", line);
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                return DBProcessResponse.builder()
                        .bytes(0)
                        .success(true)
                        .msg("Восстановление выполнено успешно")
                        .build();
            } else {
                return DBProcessResponse.builder()
                        .bytes(0)
                        .success(false)
                        .msg("Восстановление не удалось")
                        .build();
            }

        } catch (Exception e) {
            log.error("Ошибка при восстановлении", e);
            return DBProcessResponse.builder()
                    .bytes(0)
                    .success(false)
                    .msg("Восстановление не удалось: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Извлечение имени базы данных из URL
     *
     * @param url JDBC URL
     * @return имя базы данных
     */
    private String getDatabaseNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * Поиск последней резервной копии
     *
     * @return путь к последнему файлу резервной копии или null, если не найдено
     */
    private String findLatestBackup() {
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(resourceLoader);
            String pattern = "file:" + BACKUP_PATH + BACKUP_FILE + "_*";
            Resource[] resources = resolver.getResources(pattern);

            if (resources.length == 0) {
                return null;
            }

            Resource latestResource = resources[0];
            long latestModified = latestResource.lastModified();

            for (Resource resource : resources) {
                long lastModified = resource.lastModified();
                if (lastModified > latestModified) {
                    latestModified = lastModified;
                    latestResource = resource;
                }
            }

            return latestResource.getFile().getAbsolutePath();

        } catch (Exception e) {
            log.error("Ошибка при поиске последней резервной копии", e);
            return null;
        }
    }
}
