package com.dummy.myerp.consumer.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.technical.exception.NotFoundException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("../applicationContext.xml")
class ComptabiliteDaoImplIT
{
	@Autowired ComptabiliteDao daoUnderTest;
	EcritureComptable ecritureComptableExpected;
	
	@BeforeEach
	void setUp()
	{
		ecritureComptableExpected = new EcritureComptable();
		ecritureComptableExpected.setJournal(new JournalComptable("TT", "Test"));
	}

	@AfterEach
	void tearDown()
	{
		ecritureComptableExpected = null;
	}
	
	@Order(1)
	@Test
	void getJournaux_ShouldReturn_ListeJournaux()
	{
		// arrange
		JournalComptable journal = new JournalComptable("AC",  "Achat");
				
		// act
		List<JournalComptable> journaux = daoUnderTest.getListJournalComptable();
		
		// assert
		assertThat(journaux.contains(journal));
	}

	@Order(2)
	@Test
	void getComptes_ShouldReturn_ListeComptes()
	{
		// arrange
		CompteComptable compte = new CompteComptable(401, "Fournisseurs");
				
		// act
		List<CompteComptable> comptes =  daoUnderTest.getListCompteComptable();
		
		// assert
		assertThat(comptes.contains(compte));
		
	}
	
	@Order(3)
	@Test
	void getEcritures_ShouldReturn_ListeEcrituress()
	{
		// arrange
		
		// act
		List<EcritureComptable> ecritures = daoUnderTest.getListEcritureComptable();
		
		// assert
		assertThat(ecritures.size()>0);
	}
}
