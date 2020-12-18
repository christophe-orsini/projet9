package com.dummy.myerp.consumer.db.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dummy.myerp.technical.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
class ResultHelperTest
{
	@Mock private ResultSet rsMock;

	@Test
	void getInteger_WhenGetColonne_ShouldReturn9() throws SQLException
	{
		// arrange
		Mockito.when(rsMock.getInt("colonne")).thenReturn(9);
		Mockito.when(rsMock.wasNull()).thenReturn(false);
		
		// act
		Integer actual = ResultSetHelper.getInteger(rsMock,  "colonne");
		
		// assert
		assertThat(actual).isEqualTo(9);	
	}

	@Test
	void getInteger_WhenGetColonne_ShouldReturnNull() throws SQLException
	{
		// arrange
		Mockito.when(rsMock.getInt("colonne")).thenReturn(9);
		Mockito.when(rsMock.wasNull()).thenReturn(true);
		
		// act
		Integer actual = ResultSetHelper.getInteger(rsMock,  "colonne");
		
		// assert
		assertThat(actual).isEqualTo(null);	
	}
	
	@Test
	void getLong_WhenGetColonne_ShouldReturn9() throws SQLException
	{
		// arrange
		Mockito.when(rsMock.getLong("colonne")).thenReturn(9L);
		Mockito.when(rsMock.wasNull()).thenReturn(false);
		
		// act
		Long actual = ResultSetHelper.getLong(rsMock,  "colonne");
		
		// assert
		assertThat(actual).isEqualTo(9L);	
	}

	@Test
	void getLong_WhenGetColonne_ShouldReturnNull() throws SQLException
	{
		// arrange
		Mockito.when(rsMock.getLong("colonne")).thenReturn(9L);
		Mockito.when(rsMock.wasNull()).thenReturn(true);
		
		// act
		Long actual = ResultSetHelper.getLong(rsMock,  "colonne");
		
		// assert
		assertThat(actual).isEqualTo(null);	
	}
	
	@SuppressWarnings("deprecation")
	@Test
	void getDate_WhenGetColonne_ShouldReturnDate() throws SQLException
	{
		// arrange
		Mockito.when(rsMock.getDate("colonne")).thenReturn(new Date(2021, 1, 1));
		
		// act
		java.util.Date actual = ResultSetHelper.getDate(rsMock,  "colonne");
		
		// assert
		assertThat(actual).isEqualTo(new Date(2021, 1, 1));	
	}

	@Test
	void getDate_WhenGetColonne_ShouldReturnNull() throws SQLException
	{
		// arrange
		Mockito.when(rsMock.getDate("colonne")).thenReturn(null);
		
		// act
		java.util.Date actual = ResultSetHelper.getDate(rsMock,  "colonne");
		
		// assert
		assertThat(actual).isEqualTo(null);	
	}
}
