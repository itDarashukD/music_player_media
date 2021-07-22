package com.example.music_player.repository;

import com.example.music_player.MusicPlayerApplication;
import com.example.music_player.entity.Album;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ActiveProfiles;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = {MusicPlayerApplication.class})
@ActiveProfiles(profiles = "test")
public class AlbumRepositoryTest {

    private static Album album1;
    private static Album album2;
    private static Album album3;
    private RowMapper<Album> ROW_MAPPER;

    private final String SELECT_ALL_FROM_ALBUM = "SELECT * FROM album ";
    private final String DELETE_ALL_FROM_ALBUM = "DELETE FROM album";
    private final String RESTART_COUNTER_ID_IN_TABLE_WITH_1 = "ALTER TABLE album AUTO_INCREMENT = 1;";

    private final IAlbumRepository repository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AlbumRepositoryTest(IAlbumRepository repository, JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEachMethod() {
        deleteDataFromTable();
        prepareRowMapper();
        restartCountingIdInTable();
    }

    public void deleteDataFromTable() {
        jdbcTemplate.execute(DELETE_ALL_FROM_ALBUM);
    }

    public static void prepareData() {
        album1 = new Album(1L, "album1", 2001, "note1");
        album2 = new Album(2L, "album2", 2002, "note2");
        album3 = new Album(3L, "album3", 2003, "note3");
    }

    public void restartCountingIdInTable() {
        jdbcTemplate.execute(RESTART_COUNTER_ID_IN_TABLE_WITH_1);
    }

    public void insertListRowsInTable(List<Album> input) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO album (id,name,year,notes) VALUES( ? ,?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, input.get(i).getId());
                        ps.setString(2, input.get(i).getName());
                        ps.setInt(3, input.get(i).getYear());
                        ps.setString(4, input.get(i).getNotes());
                    }

                    public int getBatchSize() {
                        return input.size();
                    }
                });
    }

    public void prepareRowMapper() {
        ROW_MAPPER = (ResultSet resultSet, int rowNum) -> {
            return new Album(resultSet.getLong("id")
                    , resultSet.getString("name")
                    , resultSet.getInt("year")
                    , resultSet.getString("notes"));
        };
    }

    public static Stream<Arguments> prepareDataForDeleteById() {
        prepareData();
        return Stream.of(
                Arguments.of(List.of(album1, album2), album1, List.of(album2)),
                Arguments.of(List.of(album1, album2, album3), album3, List.of(album1, album2)),
                Arguments.of(List.of(album1, album2, album3), album3, List.of(album1, album2))
        );
    }

    private static Stream<Arguments> prepareDataForFinedAllAndAddAlbum() {
        prepareData();
        return Stream.of(
                Arguments.of(List.of(album1, album2), List.of(album1, album2)),
                Arguments.of(List.of(album1, album2, album3), List.of(album1, album2, album3))
        );
    }

    public static Stream<Arguments> prepareDataForFinedById() {
        prepareData();
        return Stream.of(
                Arguments.of(List.of(album1, album2, album3), album1),
                Arguments.of(List.of(album3, album2, album1), album3)
        );
    }

    public static Stream<Arguments> prepareDataForUpdate() {
        prepareData();
        return Stream.of(
                Arguments.of(List.of(album1), album2, album2),
                Arguments.of(List.of(album3), album1, album1)
        );
    }

    @ParameterizedTest
    @MethodSource("prepareDataForFinedAllAndAddAlbum")
    void findAllAlbums(List<Album> input, List<Album> output) {
        insertListRowsInTable(input);
        List<Album> albumList = repository.findAll();
        assertNotNull(albumList);
        assertEquals(albumList, output);
    }

    @ParameterizedTest
    @MethodSource("prepareDataForFinedAllAndAddAlbum")
    void addAlbum(List<Album> input, List<Album> output) {
        List<Album> listBeforeTest = jdbcTemplate.query(SELECT_ALL_FROM_ALBUM, ROW_MAPPER);
        assertTrue(listBeforeTest.isEmpty());
        input.forEach(x -> repository.save(x));
        List<Album> expectedList = jdbcTemplate.query(SELECT_ALL_FROM_ALBUM, ROW_MAPPER);
        assertNotNull(expectedList);
        assertEquals(expectedList, output);
    }

    @ParameterizedTest
    @MethodSource("prepareDataForFinedById")
    void findById(List<Album> input, Album output) {
        insertListRowsInTable(input);
        Album testAlbum1 = repository.findById(input.get(0).getId());
        assertThat(testAlbum1).isEqualTo(output);
    }

    @ParameterizedTest
    @MethodSource("prepareDataForDeleteById")
    public void deleteById(List<Album> input, Album toDelete, List<Album> output) {
        insertListRowsInTable(input);
        repository.deleteById(toDelete.getId());
        List<Album> expectedList = jdbcTemplate.query("select * from album", ROW_MAPPER);
        assertEquals(expectedList, output);
    }

    @ParameterizedTest
    @MethodSource("prepareDataForUpdate")
    void update(List<Album> input, Album instanceForUpdate, Album output) {
        insertListRowsInTable(input);
        Album testAlbum = input.get(0);
        testAlbum.setName(instanceForUpdate.getName());
        testAlbum.setNotes(instanceForUpdate.getNotes());
        testAlbum.setYear(instanceForUpdate.getYear());
        repository.update(testAlbum);

        List<Album> expectedList = jdbcTemplate.query("select * from album WHERE name = 'album2'", ROW_MAPPER);

        assertEquals(expectedList.get(0).getNotes(), output.getNotes());
        assertEquals(expectedList.get(0).getName(), output.getName());
        assertEquals(expectedList.get(0).getYear(), output.getYear());



    }
}