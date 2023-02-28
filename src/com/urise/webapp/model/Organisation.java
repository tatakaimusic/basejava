package com.urise.webapp.model;

import com.urise.webapp.util.DateUtil;
import com.urise.webapp.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static com.urise.webapp.util.DateUtil.NOW;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organisation implements Serializable {
    @Serial
    private static final long serialVersionUID = -313169696235072244L;
    private Link link;
    private List<Period> periods = new ArrayList<>();

    public Organisation(String name, String url, Period... periods) {
        this(new Link(name, url), Arrays.asList(periods));
    }

    public Organisation(Link name, List<Period> periods) {
        this.link = name;
        this.periods = periods;
    }

    public Organisation() {
    }

    public Link getLink() {
        return link;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    public void setPeriod(Period period) {
        periods.add(period);
    }

    @Override
    public String toString() {
        return "Organisation{" + link + '\'' + periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organisation that = (Organisation) o;
        return link.equals(that.link) && periods.equals(that.periods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, periods);
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Period implements Serializable {
        @Serial
        private static long serialVersionUID = -313169696235072244L;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate startDate;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate finishDate;
        private String title;
        private String description;

        public Period(int startYear, Month startMonth, String title, String description) {
            this(DateUtil.of(startYear, startMonth), NOW, title, description);
        }

        public Period() {
        }

        public Period(int startYear, Month startMonth, int finishYear, Month finishMonth, String title, String description) {
            this(DateUtil.of(startYear, startMonth), DateUtil.of(finishYear, finishMonth), title, description);
        }

        public Period(LocalDate startDate, LocalDate finishDate, String title, String description) {
            Objects.requireNonNull(startDate, "Start Date must not be null");
            Objects.requireNonNull(finishDate, "Finish Date must not be null");
            Objects.requireNonNull(title, "Title must not be null");
            this.startDate = startDate;
            this.finishDate = finishDate;
            this.title = title;
            this.description = description == null ? "" : description;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getFinishDate() {
            return finishDate;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }


        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public void setFinishDate(LocalDate finishDate) {
            this.finishDate = finishDate;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "Period{" +
                    startDate + " " +
                    finishDate + " " +
                    title + '\'' + " " +
                    description + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Period period = (Period) o;
            return startDate.equals(period.startDate) && finishDate.equals(period.finishDate) && title.equals(period.title) && Objects.equals(description, period.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(startDate, finishDate, title, description);
        }
    }
}
