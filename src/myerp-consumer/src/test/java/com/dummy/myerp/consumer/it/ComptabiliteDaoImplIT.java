package com.dummy.myerp.consumer.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("../applicationContext.xml")
class ComptabiliteDaoImplIT
{
	@Autowired ComptabiliteDao daoUnderTest;
		
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
	
	@Test
	void getEcritures_ShouldReturn_ListeEcritures()
	{
		// arrange
		
		// act
		List<EcritureComptable> ecritures = daoUnderTest.getListEcritureComptable();
		
		// assert
		assertThat(ecritures.size()>0);
	}
	
	@Test
	void getEcriture_ShouldReturn_EcritureComptable() throws NotFoundException
	{
		// arrange
		
		// act
		EcritureComptable ecriture = daoUnderTest.getEcritureComptable(-1);
		
		// assert
		assertThat(ecriture.getId()).isEqualTo(-1);
		
		// act
		EcritureComptable otherEcriture = daoUnderTest.getEcritureComptableByRef(ecriture.getReference());
				
		// assert
		assertThat(otherEcriture.getId()).isEqualTo(-1);
	}
	
	@Test
	void getEcriture_ShouldRaiseException()
	{
		// assert
        assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> {
        	daoUnderTest.getEcritureComptable(-10000);})
        	.withMessage("EcritureComptable non trouvée : id=-10000");
        
	    // assert
	    assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> {
	    	daoUnderTest.getEcritureComptableByRef("dummy");})
	    	.withMessage("EcritureComptable non trouvée : reference=dummy");
	}
	
	@Test
	void getSequenceEcriture_ShouldReturn_SequenceEcritureComptable() throws NotFoundException
	{
		// arrange
		
		// act
		SequenceEcritureComptable sequence = daoUnderTest.getSequenceEcritureComptableByJournalAnnee("BQ", 2016);
		
		// assert
		assertThat(sequence.getAnnee()).isEqualTo(2016);
	}
	
	@Test
	void getSequenceEcriture_ShouldRaiseException()
	{
		// assert
        assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> {
        	daoUnderTest.getSequenceEcritureComptableByJournalAnnee("BQ", 2130);})
        	.withMessage("SequenceEcritureComptable non trouvée : codeJournal=BQ / annee=2130");
	}
	
	@Test
	void updateSequenceEcriture_ShouldUpdate() throws NotFoundException
	{
		// arrange
		SequenceEcritureComptable sequence = daoUnderTest.getSequenceEcritureComptableByJournalAnnee("BQ", 2016);
		int derniereValeur = sequence.getDerniereValeur();
		derniereValeur++;
		sequence.setDerniereValeur(derniereValeur);
		
		// act
		daoUnderTest.updateSequenceEcritureComptable(sequence);
		SequenceEcritureComptable actualSequence = daoUnderTest.getSequenceEcritureComptableByJournalAnnee("BQ", 2016);
		
		// assert
		assertThat(actualSequence.getDerniereValeur()).isEqualTo(derniereValeur);
	}
	
}
