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

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;

@ExtendWith(MockitoExtension.class)
class CompteComptableRMTest
{
	RowMapper<CompteComptable> rowMapperUnderTest = new CompteComptableRM();
	
	@Mock ResultSet resultSetMock;
	
	@Test
	void mapRow_ShouldReturnCompteComptable() throws SQLException
	{
		// arrange
		Mockito.when(resultSetMock.getInt("numero")).thenReturn(1);
		Mockito.when(resultSetMock.getString("libelle")).thenReturn("Libellé");
		CompteComptable expected = new CompteComptable(1, "Libellé");
		
		// act
		CompteComptable actual = rowMapperUnderTest.mapRow(resultSetMock, 1);
		
		// assert
		assertThat(actual.getNumero()).isEqualTo(expected.getNumero());
		assertThat(actual.getLibelle()).isEqualTo(expected.getLibelle());
	}

}
