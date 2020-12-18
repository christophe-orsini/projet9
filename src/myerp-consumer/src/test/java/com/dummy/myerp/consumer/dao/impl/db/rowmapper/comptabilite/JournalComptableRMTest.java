package com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite;

import static org.assertj.core.api.Assertions.assertThat;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;

@ExtendWith(MockitoExtension.class)
class JournalComptableRMTest
{
	RowMapper<JournalComptable> rowMapperUnderTest = new JournalComptableRM();
	
	@Mock ResultSet resultSetMock;
	
	@Test
	void mapRow_ShouldReturnJournalComptable() throws SQLException
	{
		// arrange
		Mockito.when(resultSetMock.getString("code")).thenReturn("BQ");
		Mockito.when(resultSetMock.getString("libelle")).thenReturn("Banque");
		JournalComptable expected = new JournalComptable("BQ", "Banque");
		
		// act
		JournalComptable actual = rowMapperUnderTest.mapRow(resultSetMock, 1);
		
		// assert
		assertThat(actual.getCode()).isEqualTo(expected.getCode());
		assertThat(actual.getLibelle()).isEqualTo(expected.getLibelle());
	}

}
