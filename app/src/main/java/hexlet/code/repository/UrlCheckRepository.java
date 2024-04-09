package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

public class UrlCheckRepository extends BaseRepository {
    public static void save(UrlCheck urlCheck) throws SQLException {
        String sql = "INSERT INTO url_checks (url_id, status_code, title, h1, description, created_at)"
                + " VALUES (?, ?, ?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, urlCheck.getUrlId());
            stmt.setInt(2, urlCheck.getStatusCode());
            stmt.setString(3, urlCheck.getTitle());
            stmt.setString(4, urlCheck.getH1());
            stmt.setString(5, urlCheck.getDescription());
            stmt.setTimestamp(6, urlCheck.getCreatedAt());
            stmt.executeUpdate();
            var generatedKeys = stmt.getGeneratedKeys();

            if (generatedKeys.next()) {
                urlCheck.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static List<UrlCheck> getEntitiesById(Long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            var checks = stmt.executeQuery();
            var result = new ArrayList<UrlCheck>();

            while (checks.next()) {
                var id = checks.getLong("id");
                var statusCode = checks.getInt("status_code");
                var title = checks.getString("title");
                var h1 = checks.getString("h1");
                var description = checks.getString("description");
                var createdAt = checks.getTimestamp("created_at");
                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId, createdAt);
                urlCheck.setId(id);
                result.add(urlCheck);
            }
            return result;
        }
    }

    public static Optional<UrlCheck> getLastChecks(Long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC LIMIT 1";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            var checks = stmt.executeQuery();

            if (checks.next()) {
                var id = checks.getLong("id");
                var statusCode = checks.getInt("status_code");
                var title = checks.getString("title");
                var h1 = checks.getString("h1");
                var description = checks.getString("description");
                var createdAt = checks.getTimestamp("created_at");
                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId, createdAt);
                urlCheck.setId(id);
                return Optional.of(urlCheck);
            } else {
                return Optional.empty();
            }
        }
    }

    public static Map<Long, UrlCheck> getAllLastChecks() throws SQLException {
        var sql = "SELECT * FROM url_checks ORDER BY created_at DESC";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql);
             var checks = stmt.executeQuery()) {
            Map<Long, UrlCheck> result = new HashMap<>();

            while (checks.next()) {
                var id = checks.getLong("id");
                var statusCode = checks.getInt("status_code");
                var title = checks.getString("title");
                var h1 = checks.getString("h1");
                var description = checks.getString("description");
                var createdAt = checks.getTimestamp("created_at");
                var urlId = checks.getLong("url_id");
                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId, createdAt);
                urlCheck.setId(id);
                result.put(urlId, urlCheck);
            }

            return result;
        }
    }
}
