package se.umu.cs.ldbn.server.dao.typehandlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@MappedTypes(Date.class)
public class DateTypeHandler extends BaseTypeHandler<Date> {
	
	private static final Logger log = LoggerFactory.getLogger(DateTypeHandler.class);

	private final ThreadLocal<DateFormat> formatter = new ThreadLocal<DateFormat>() {
		@Override 
		protected DateFormat initialValue() {
	        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	    }
		
	};

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
    	ps.setTimestamp(i, new Timestamp((parameter).getTime()));
    }

    @Override
    public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
        final String s = rs.getString(columnName);
        return fromString(s);
    }

    @Override
    public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    	final String s = rs.getString(columnIndex);
        return fromString(s);
    }

    @Override
    public Date getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        final String s = cs.getString(columnIndex);
        return fromString(s);
    }
    
    private Date fromString(String s) {
    	if (s == null) {
    		return null;
    	}
    	try {
			return formatter.get().parse(s);
		} catch (ParseException e) {
			log.error("Caanot parse date:", e);
			return null;
		}
    }

}
