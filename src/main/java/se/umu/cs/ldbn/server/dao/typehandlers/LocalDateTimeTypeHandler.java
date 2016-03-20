package se.umu.cs.ldbn.server.dao.typehandlers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

/**
 * Map Java 8 LocalDateTime &lt;-&gt; java.sql.Timestamp
 */
@MappedTypes(LocalDateTime.class)
public class LocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {
	
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setTimestamp(i,
            Timestamp.valueOf(parameter),
            GregorianCalendar.from(ZonedDateTime.of(parameter, ZoneId.systemDefault()))
        );
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        final String s = rs.getString(columnName);
        return fromString(s);
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    	final String s = rs.getString(columnIndex);
        return fromString(s);
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        final String s = cs.getString(columnIndex);
        return fromString(s);
    }
    
    private LocalDateTime fromString(String s) {
    	if (s == null) {
    		return null;
    	}
    	return LocalDateTime.parse(s, FORMATTER);
    }
}