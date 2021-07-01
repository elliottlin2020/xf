package com.mycompany.dashboard.system.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.dashboard.system.domain.AnnouncementRecord} entity. This class is used
 * in {@link com.mycompany.dashboard.system.web.rest.AnnouncementRecordResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /announcement-records?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AnnouncementRecordCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter anntId;

    private LongFilter userId;

    private BooleanFilter hasRead;

    private ZonedDateTimeFilter readTime;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    public AnnouncementRecordCriteria() {}

    public AnnouncementRecordCriteria(AnnouncementRecordCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.anntId = other.anntId == null ? null : other.anntId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.hasRead = other.hasRead == null ? null : other.hasRead.copy();
        this.readTime = other.readTime == null ? null : other.readTime.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
    }

    @Override
    public AnnouncementRecordCriteria copy() {
        return new AnnouncementRecordCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getAnntId() {
        return anntId;
    }

    public LongFilter anntId() {
        if (anntId == null) {
            anntId = new LongFilter();
        }
        return anntId;
    }

    public void setAnntId(LongFilter anntId) {
        this.anntId = anntId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public BooleanFilter getHasRead() {
        return hasRead;
    }

    public BooleanFilter hasRead() {
        if (hasRead == null) {
            hasRead = new BooleanFilter();
        }
        return hasRead;
    }

    public void setHasRead(BooleanFilter hasRead) {
        this.hasRead = hasRead;
    }

    public ZonedDateTimeFilter getReadTime() {
        return readTime;
    }

    public ZonedDateTimeFilter readTime() {
        if (readTime == null) {
            readTime = new ZonedDateTimeFilter();
        }
        return readTime;
    }

    public void setReadTime(ZonedDateTimeFilter readTime) {
        this.readTime = readTime;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            createdBy = new StringFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            createdDate = new InstantFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new StringFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new InstantFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getJhiCommonSearchKeywords() {
        return jhiCommonSearchKeywords;
    }

    public void setJhiCommonSearchKeywords(String jhiCommonSearchKeywords) {
        this.jhiCommonSearchKeywords = jhiCommonSearchKeywords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AnnouncementRecordCriteria that = (AnnouncementRecordCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(anntId, that.anntId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(hasRead, that.hasRead) &&
            Objects.equals(readTime, that.readTime) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, anntId, userId, hasRead, readTime, createdBy, createdDate, lastModifiedBy, lastModifiedDate);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnnouncementRecordCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (anntId != null ? "anntId=" + anntId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (hasRead != null ? "hasRead=" + hasRead + ", " : "") +
                (readTime != null ? "readTime=" + readTime + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
                (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            "}";
    }
}
