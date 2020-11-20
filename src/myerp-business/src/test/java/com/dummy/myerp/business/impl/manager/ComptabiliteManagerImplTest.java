package com.dummy.myerp.business.impl.manager;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Calendar;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
class ComptabiliteManagerImplTest
{
	@Mock private EcritureComptable ecritureComptableMock;
	@Mock private DaoProxy daoProxyMock;
	@Mock private ComptabiliteDao comptabiliteDaoMock;
	
	private ComptabiliteManagerImpl managerUnderTest;
	
	@BeforeEach
	void setUp() throws Exception
	{
    	managerUnderTest = new ComptabiliteManagerImpl();
    	managerUnderTest.configure(null, daoProxyMock, null);
	}
	
	@AfterEach
	void CleanUp()
	{
		managerUnderTest = null;
	}

	@Test
	void addReference_AlreadyExist()
	{
		// arrange
		Calendar calendar = Calendar.getInstance();
		String annee = String.valueOf(calendar.get(Calendar.YEAR));
		String expected = "AC-" + annee + "/00001";
		
		Mockito.when(ecritureComptableMock.getReference()).thenReturn(expected);
				
		// act
		managerUnderTest.addReference(ecritureComptableMock);
		
		// assert
		Mockito.verify(ecritureComptableMock).getReference();
	}
	
	@Test
	void addReference_EcritureComptableExists_ShouldHaveReference() throws NotFoundException
	{
		// arrange
		Calendar calendar = Calendar.getInstance();
		String annee = String.valueOf(calendar.get(Calendar.YEAR));
		String expected = "AC-" + annee + "/00006";
		
		EcritureComptable ecritureComptable = Mockito.spy(new EcritureComptable());
		ecritureComptable.setDate(new Date());
		ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		
		Mockito.when(daoProxyMock.getComptabiliteDao()).thenReturn(comptabiliteDaoMock);
		Mockito.when(comptabiliteDaoMock.getSequenceEcritureComptableByJournalAnnee(Mockito.anyString(), Mockito.anyInt())).
				thenReturn(new SequenceEcritureComptable("AC", calendar.get(Calendar.YEAR), 5));
		
		// act
		managerUnderTest.addReference(ecritureComptable);
		
		// assert
		assertThat(ecritureComptable.getReference()).isEqualTo(expected);
	}

	@Test
	void addReference_EcritureComptableNotExists_ShouldHaveReference() throws NotFoundException
	{
		// arrange
		Calendar calendar = Calendar.getInstance();
		String annee = String.valueOf(calendar.get(Calendar.YEAR));
		String expected = "AC-" + annee + "/00001";
		
		EcritureComptable ecritureComptable = Mockito.spy(new EcritureComptable());
		ecritureComptable.setDate(new Date());
		ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		
		Mockito.when(daoProxyMock.getComptabiliteDao()).thenReturn(comptabiliteDaoMock);
		Mockito.when(comptabiliteDaoMock.getSequenceEcritureComptableByJournalAnnee(Mockito.anyString(), Mockito.anyInt())).
				thenThrow(NotFoundException.class);
		
		// act
		managerUnderTest.addReference(ecritureComptable);
		
		// assert
		assertThat(ecritureComptable.getReference()).isEqualTo(expected);
	}
}
