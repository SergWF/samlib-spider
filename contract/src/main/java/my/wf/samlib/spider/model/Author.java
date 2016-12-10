package my.wf.samlib.spider.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Author {
    private String url;
    private String name;
    private Set<Writing> writings = new HashSet<>();

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

    public Set<Writing> getWritings() {
        return writings;
    }

    public void setWritings(Set<Writing> writings) {
        this.writings.clear();
        this.writings.addAll(writings);
    }

    public LocalDateTime getLastUpdated() {
        return writings.stream()
                       .map(Writing::getLastUpdated)
                       .max(LocalDateTime::compareTo)
                       .orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Author)) { return false; }

        Author author = (Author) o;

        return url != null ? url.equals(author.url) : super.equals(o);

    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return "Author{" +
                "url='" + url + '\'' +
                '}';
    }
}
