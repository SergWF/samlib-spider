package my.wf.samlib.spider.engine;

import my.wf.samlib.spider.model.Author;
import my.wf.samlib.spider.model.Changes;
import my.wf.samlib.spider.model.Writing;
import my.wf.samlib.spider.model.WritingData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthorChangeHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthorChangeHandler.class);
    private final Author author;
    private LocalDateTime updateTime;

    AuthorChangeHandler(Author author, LocalDateTime updateTime) {
        this.author = author;
        this.updateTime = updateTime;
    }

    String getFullUrl(Author author, String writingUrl) {
        return author.getUrl() + writingUrl;
    }

    Writing addNewWriting(WritingData writingData) {
        Writing writing = createWriting(writingData);
        author.getWritings()
              .add(writing);
        return writing;
    }

    Writing createWriting(WritingData writingData) {
        Writing writing = updateWriting(new Writing(getFullUrl(author, writingData.getUrl())), writingData);
        writing.getChanges()
               .clear();
        writing.getChanges()
               .add(Changes.NEW);
        return writing;
    }

    Writing updateWriting(Writing writing, WritingData writingData) {
        writing.getChanges()
               .clear();
        if (!writing.getDescription()
                    .equals(writingData.getDescription())) {
            writing.getChanges()
                   .add(Changes.DESCRIPTION);
        }
        if (!writing.getSize()
                    .equals(writingData.getSize())) {
            writing.getChanges()
                   .add(Changes.SIZE);
        }
        if (!writing.getName()
                    .equals(writingData.getName())) {
            writing.getChanges()
                   .add(Changes.NAME);
        }
        writing.setDescription(writingData.getDescription());
        writing.setName(writingData.getName());
        writing.setSize(writingData.getSize());
        writing.setGroupName(writingData.getGroupName());
        return writing;
    }

    Optional<Writing> findWritingByData(Author author, WritingData data) {
        return author.getWritings()
                     .stream()
                     .filter(writing -> writing.getUrl()
                                               .equals(data.getUrl()))
                     .findFirst();
    }

    public void handleDeleted(Author author, Collection<WritingData> writingDatas) {
        Set<String> existingUrls = writingDatas.stream()
                                               .map(writingData -> writingData.getUrl())
                                               .collect(Collectors.toSet());
        int removed = author.getWritings()
                            .size();
        boolean remove = author.getWritings()
                               .removeIf(writing -> !existingUrls.contains(writing.getUrl()));
        removed = removed - author.getWritings()
                                  .size();
        if (remove) {
            logger.debug("removed {} writings", removed);
        }
    }

    public Writing handleWritingUpdate(WritingData writingData) {
        Optional<Writing> writingByData = findWritingByData(author, writingData);
        Writing writing = writingByData.isPresent()
                ? updateWriting(writingByData.get(), writingData)
                : addNewWriting(writingData);
        if (!writing.getChanges()
                    .isEmpty()) {
            writing.setLastUpdated(updateTime);
        }
        return writing;
    }
}
