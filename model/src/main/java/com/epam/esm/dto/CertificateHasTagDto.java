package com.epam.esm.dto;

public class CertificateHasTagDto {

    private String certificateId;
    private String tagId;

    public CertificateHasTagDto() {}

    public CertificateHasTagDto(String certificateId, String tagId) {
        this.certificateId = certificateId;
        this.tagId = tagId;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CertificateHasTagDto that = (CertificateHasTagDto) obj;
        return certificateId.equals(that.certificateId)
                && tagId.equals(that.tagId);
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;

        result = result * prime + (certificateId != null ? certificateId.hashCode() : 0);
        result = result * prime + (tagId != null ? tagId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CertificateHasTagDto { ")
                .append("certificateId = '").append(certificateId).append('\'')
                .append(", tagId = '").append(tagId).append('\'')
                .append(" }\n");
        return stringBuilder.toString();
    }
}
