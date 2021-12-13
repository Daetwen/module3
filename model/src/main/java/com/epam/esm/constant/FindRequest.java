package com.epam.esm.constant;

public final class FindRequest {

    public static final String FIND_CERTIFICATE_BY_PART_OF_NAME_OR_DESCRIPTION =
            "SELECT id, name, description, price, duration, create_date, last_update_date " +
            "FROM gift_certificates " +
            "WHERE name LIKE ? OR description LIKE ? ";

    public static final String FIND_CERTIFICATE_BY_TAG_NAME =
            "SELECT gift_certificates.id, gift_certificates.name, description, price, duration, create_date, last_update_date " +
            "FROM gift_certificates " +
            "JOIN gift_certificates_has_tags ON gift_certificates_id = gift_certificates.id " +
            "JOIN tags ON tags_id = tags.id " +
            "WHERE tags.name=? ";

    public static final String SORT_BY_NAME = "ORDER BY name ";

    private FindRequest() {}
}
