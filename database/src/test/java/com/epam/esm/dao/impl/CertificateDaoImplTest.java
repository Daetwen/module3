package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CertificateDaoImplTest {
//
//    private CertificateDao certificateDao;
//    private Certificate certificate1;
//    private Certificate certificate4;
//    private List<Certificate> certificates;
//    private List<Certificate> certificates2;
//    public static final String FIND_CERTIFICATE_BY_PART_OF_NAME_OR_DESCRIPTION =
//            "SELECT id, name, description, price, duration, create_date, last_update_date " +
//            "FROM gift_certificates " +
//            "WHERE name LIKE ? OR description LIKE ? ";
//
//    @BeforeAll
//    public void setUp() {
//        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
//                .addScript("classpath:gift_certificates_table.sql")
//                .addScript("classpath:gift_certificates_data.sql")
//                .build();
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        certificateDao = new CertificateDaoImpl(jdbcTemplate);
//
//        List<Tag> tags = new ArrayList<>();
//        certificate1 = new Certificate(1L, "Certificate1", "hello", new BigDecimal(5000), 50,
//                OffsetDateTime.parse("2021-11-20T21:30:19+03:00"),
//                OffsetDateTime.parse("2021-11-20T21:30:19+03:00"), tags);
//        Certificate certificate2 = new Certificate(2L, "Certificate2", "hello world", new BigDecimal(6000), 60,
//                OffsetDateTime.parse("2021-11-20T21:30:19+03:00"),
//                OffsetDateTime.parse("2021-11-20T21:30:19+03:00"), tags);
//        Certificate certificate3 = new Certificate(3L, "Certificate3", "hello someone", new BigDecimal(7000), 70,
//                OffsetDateTime.parse("2021-11-20T21:30:19+03:00"),
//                OffsetDateTime.parse("2021-11-20T21:30:19+03:00"), tags);
//        certificate4 = new Certificate(4L, "Certificate4", "hello Vlad", new BigDecimal(8000), 80,
//                OffsetDateTime.parse("2021-11-20T21:30:19+03:00"),
//                OffsetDateTime.parse("2021-11-20T21:30:19+03:00"), tags);
//
//        certificates = new ArrayList<>();
//        certificates.add(certificate1);
//        certificates.add(certificate2);
//        certificates.add(certificate3);
//
//        certificates2 = new ArrayList<>();
//        certificates2.add(certificate2);
//    }
//
//    @Test
//    public void findByIdTestTrue() {
//        Optional<Certificate> expected = Optional.of(certificate1);
//        Optional<Certificate> actual = certificateDao.findById(1L);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void findByIdTestFalse() {
//        Optional<Certificate> expected = Optional.empty();
//        Optional<Certificate> actual = certificateDao.findById(10L);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void findAllTestTrue() {
//        List<Certificate> expected = certificates;
//        List<Certificate> actual = certificateDao.findAll();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void findByParametersTestTrue() {
//        List<Certificate> expected = certificates2;
//        Object[] arguments = {"%ate2%", "%ate2%"};
//        List<Certificate> actual = certificateDao.findByParameters(
//                        FIND_CERTIFICATE_BY_PART_OF_NAME_OR_DESCRIPTION, arguments);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void updateTestTrue1() {
//        int expected = 1;
//        certificate1.setName("Vladislav");
//        int actual = certificateDao.update(certificate1);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void updateTestTrue2() {
//        int expected = 1;
//        certificate1.setName("Certificate1");
//        int actual = certificateDao.update(certificate1);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void updateTestFalse1() {
//        int expected = 0;
//        int actual = certificateDao.update(certificate4);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void deleteByIdTestTrue() {
//        int expected = 1;
//        int actual = certificateDao.deleteById(1L);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void deleteByIdTestFalse() {
//        int expected = 0;
//        int actual = certificateDao.deleteById(55L);
//        assertEquals(expected, actual);
//    }
}
