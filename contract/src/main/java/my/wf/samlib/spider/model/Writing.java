package my.wf.samlib.spider.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Writing {
    private String url;
    private String name = "";
    private String description = "";
    private String size = "";
    private String groupName;
    private LocalDateTime lastUpdated;

    private Set<Changes> changes = new HashSet<>();

    public Writing() {
    }

    public Writing(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Set<Changes> getChanges() {
        return changes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Writing)) { return false; }

        Writing writing = (Writing) o;

        return url != null ? url.equals(writing.url) : super.equals(o);

    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return "Writing{" + "url='" + url + '\'' + '}';
    }
}
