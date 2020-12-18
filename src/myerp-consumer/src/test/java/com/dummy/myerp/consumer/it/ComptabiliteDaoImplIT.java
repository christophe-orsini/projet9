package com.dummy.myerp.consumer.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
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
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("../applicationContext.xml")
class ComptabiliteDaoImplIT
{
	@Autowired ComptabiliteDao daoUnderTest;
		
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
	void getEcritures_ShouldReturn_ListeEcritures()
	{
		// arrange
		
		// act
		List<EcritureComptable> ecritures = daoUnderTest.getListEcritureComptable();
		
		// assert
		assertThat(ecritures.size()>0);
	}
	
	@Order(4)
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
	
	@Order(5)
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
	
	@Order(6)
	@Test
	void getSequenceEcriture_ShouldReturn_SequenceEcritureComptable() throws NotFoundException
	{
		// arrange
		
		// act
		SequenceEcritureComptable sequence = daoUnderTest.getSequenceEcritureComptableByJournalAnnee("BQ", 2016);
		
		// assert
		assertThat(sequence.getAnnee()).isEqualTo(2016);
	}
	
	@Order(7)
	@Test
	void getSequenceEcriture_ShouldRaiseException()
	{
		// assert
        assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> {
        	daoUnderTest.getSequenceEcritureComptableByJournalAnnee("BQ", 2130);})
        	.withMessage("SequenceEcritureComptable non trouvée : codeJournal=BQ / annee=2130");
	}
	
	@Order(8)
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
	
	@Order(9)
	@SuppressWarnings("deprecation")
	@Test
	public void testIntegration_EcritureComptable() throws NotFoundException
	{
		// arrange
		EcritureComptable ecriture = new EcritureComptable();
		
		JournalComptable journal = new JournalComptable("AC", "Achat");
		ecriture.setJournal(journal);
		
		SequenceEcritureComptable sequence = daoUnderTest.getSequenceEcritureComptableByJournalAnnee("AC", 2016);
		String referenceEcriture = "AC-2016/" + String.format("%05d", sequence.getDerniereValeur()+1);
		ecriture.setReference(referenceEcriture);
		ecriture.setDate(new Date(2016, 01, 01));	
		ecriture.setLibelle("Ecriture de test");
		ecriture.getListLigneEcriture().add(this.createLigne(606, "200.50", null));
    	ecriture.getListLigneEcriture().add(this.createLigne(401, null, "200.540"));
    
    	// supprime l'eciture si elle existe
    	try
    	{
    		EcritureComptable dummy = daoUnderTest.getEcritureComptableByRef(ecriture.getReference());
    		daoUnderTest.deleteEcritureComptable(dummy.getId());
    	}
    	catch (NotFoundException e)
    	{
    		// l'ecriture n'existe pas
    	}
			    
	    // act
	    daoUnderTest.insertEcritureComptable(ecriture);
	    EcritureComptable actualEcriture = daoUnderTest.getEcritureComptableByRef(referenceEcriture);
	    
	    // assert
	    assertThat(actualEcriture.getJournal().getLibelle()).isEqualTo("Achat");
	    
		// arrange
	    sequence.setDerniereValeur(sequence.getDerniereValeur() + 1);
	    daoUnderTest.updateSequenceEcritureComptable(sequence);
	    actualEcriture.setLibelle("Nouvelle écriture de test");
	    
	    // act
	    daoUnderTest.updateEcritureComptable(actualEcriture);
	    EcritureComptable updatedEcriture = daoUnderTest.getEcritureComptableByRef(referenceEcriture);
	    
	    // assert
	    assertThat(updatedEcriture.getLibelle()).isEqualTo("Nouvelle écriture de test");
	    
	    // act
	    daoUnderTest.deleteEcritureComptable(updatedEcriture.getId());
	    
	 // assert
	    assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> {
	    	daoUnderTest.getEcritureComptableByRef(ecriture.getReference());})
	    	.withMessage("EcritureComptable non trouvée : reference=" + referenceEcriture);
	}
	
	//************************* Methods
    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit)
    {
        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                                     .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
                                                                    vLibelle,
                                                                    vDebit, vCredit);
        return vRetour;
    }
}
