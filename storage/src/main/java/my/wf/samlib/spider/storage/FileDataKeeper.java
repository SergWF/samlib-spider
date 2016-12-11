package my.wf.samlib.spider.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.wf.samlib.spider.storage.error.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class FileDataKeeper implements DataKeeper {

    private static final Logger logger = LoggerFactory.getLogger(FileDataKeeper.class);
    private String filePath;
    private ObjectMapper objectMapper;

    public FileDataKeeper(String filePath, ObjectMapper objectMapper) {
        this.filePath = filePath;
        this.objectMapper = objectMapper;
    }

    @Override
    public AuthorStore load() {
        File storageFile = findOrCreateStorageFile();
        if (!storageFile.exists()) {
            logger.warn("Storage file {} not found. Create empty storage", storageFile.getAbsolutePath());
            save(new AuthorStore());
        }
        try {
            return objectMapper.readValue(storageFile, AuthorStore.class);
        } catch (IOException e) {
            throw new StorageException("Can not read file " + filePath, e);
        }
    }

    private File findOrCreateStorageFile() {
        return new File(filePath.replaceFirst("^~", System.getProperty("user.home")));
    }

    @Override
    public void save(AuthorStore authorStore) {
        File file = findOrCreateStorageFile();
        try {
            objectMapper.writeValue(file, authorStore);
        } catch (IOException e) {
            throw new StorageException("Can not write file " + file, e);
        }
    }
}
