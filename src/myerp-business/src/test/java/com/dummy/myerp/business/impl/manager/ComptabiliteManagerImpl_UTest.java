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
import org.springframework.transaction.TransactionStatus;

import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
class ComptabiliteManagerImpl_UTest
{
	@Mock private EcritureComptable ecritureComptableMock;
	@Mock private DaoProxy daoProxyMock;
	@Mock private ComptabiliteDao comptabiliteDaoMock;
	@Mock private TransactionManager transactionManagerMock;
	
	private ComptabiliteManagerImpl managerUnderTest;
	
	@BeforeEach
	void setUp() throws Exception
	{
		managerUnderTest = Mockito.spy(new ComptabiliteManagerImpl());
    	managerUnderTest.configure(null, daoProxyMock, transactionManagerMock);
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
		
	@Test
	void insertEcritureComptable() throws FunctionalException
	{
		// arrange
		TransactionStatus transactionStatusMock = Mockito.mock(TransactionStatus.class);
		Mockito.doNothing().when(managerUnderTest).checkEcritureComptable(Mockito.any(EcritureComptable.class));
		Mockito.when(transactionManagerMock.beginTransactionMyERP()).thenReturn(transactionStatusMock);
		Mockito.when(daoProxyMock.getComptabiliteDao()).thenReturn(comptabiliteDaoMock);
		Mockito.doNothing().when(comptabiliteDaoMock).insertEcritureComptable(Mockito.any(EcritureComptable.class));
		Mockito.doNothing().when(transactionManagerMock).commitMyERP(transactionStatusMock);
		Mockito.doNothing().when(transactionManagerMock).rollbackMyERP(null);
		
		// act
		managerUnderTest.insertEcritureComptable(ecritureComptableMock);
		
		// assert
		Mockito.verify(transactionManagerMock, Mockito.atLeastOnce()).commitMyERP(transactionStatusMock);
		Mockito.verify(transactionManagerMock, Mockito.atLeastOnce()).rollbackMyERP(null);
	}
	
	@Test
	void updateEcritureComptable() throws FunctionalException
	{
		// arrange
		TransactionStatus transactionStatusMock = Mockito.mock(TransactionStatus.class);
		Mockito.when(transactionManagerMock.beginTransactionMyERP()).thenReturn(transactionStatusMock);
		Mockito.when(daoProxyMock.getComptabiliteDao()).thenReturn(comptabiliteDaoMock);
		Mockito.doNothing().when(comptabiliteDaoMock).updateEcritureComptable(Mockito.any(EcritureComptable.class));
		Mockito.doNothing().when(transactionManagerMock).commitMyERP(transactionStatusMock);
		Mockito.doNothing().when(transactionManagerMock).rollbackMyERP(null);
		
		// act
		managerUnderTest.updateEcritureComptable(ecritureComptableMock);
		
		// assert
		Mockito.verify(transactionManagerMock, Mockito.atLeastOnce()).commitMyERP(transactionStatusMock);
		Mockito.verify(transactionManagerMock, Mockito.atLeastOnce()).rollbackMyERP(null);
	}
	
	@Test
	void deleteEcritureComptable() throws FunctionalException
	{
		// arrange
		TransactionStatus transactionStatusMock = Mockito.mock(TransactionStatus.class);
		Mockito.when(transactionManagerMock.beginTransactionMyERP()).thenReturn(transactionStatusMock);
		Mockito.when(daoProxyMock.getComptabiliteDao()).thenReturn(comptabiliteDaoMock);
		Mockito.doNothing().when(comptabiliteDaoMock).deleteEcritureComptable(Mockito.any(Integer.class));
		Mockito.doNothing().when(transactionManagerMock).commitMyERP(transactionStatusMock);
		Mockito.doNothing().when(transactionManagerMock).rollbackMyERP(null);
		
		// act
		managerUnderTest.deleteEcritureComptable(1);
		
		// assert
		Mockito.verify(transactionManagerMock, Mockito.atLeastOnce()).commitMyERP(transactionStatusMock);
		Mockito.verify(transactionManagerMock, Mockito.atLeastOnce()).rollbackMyERP(null);
	}
}
