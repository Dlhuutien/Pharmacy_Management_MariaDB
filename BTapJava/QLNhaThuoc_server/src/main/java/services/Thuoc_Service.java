package services;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import dao.ThuocDAO;
import entities.Thuoc;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

public class Thuoc_Service extends UnicastRemoteObject implements ThuocDAO {
	private static final long serialVersionUID = 1L;
	private EntityManager entityManager;

	public Thuoc_Service(EntityManager entityManager) throws RemoteException {
		this.entityManager = entityManager;
	}

	@Override
	public boolean updateThuoc(Thuoc thuoc) throws RemoteException {
		EntityTransaction trans = entityManager.getTransaction();
		try {
			trans.begin();
			entityManager.merge(thuoc);
			trans.commit();
			return true;
		} catch (Exception e) {
			if (trans.isActive()) {
				trans.rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Thuoc> getThuocKeDonList() throws RemoteException {
		ArrayList<Thuoc> thuocList = new ArrayList<>();
		try {
			Query query = entityManager.createQuery("SELECT t FROM Thuoc t WHERE t.keDon = true");
			thuocList = (ArrayList<Thuoc>) query.getResultList();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return thuocList;
	}

	@Override
	public int getMaxThuocID() throws RemoteException {
		int maxThuocID = 0;
		try {
			Query query = entityManager.createQuery("SELECT MAX(CAST(SUBSTRING(t.maThuoc, 3) AS integer)) FROM Thuoc t\r\n"
					+ "");
			Object result = query.getSingleResult();
			if (result != null) {
				maxThuocID = ((Number) result).intValue();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return maxThuocID;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Thuoc> getThuocKoKeDonList() throws RemoteException {
		ArrayList<Thuoc> thuocList = new ArrayList<>();
		try {
			Query query = entityManager.createQuery("SELECT t FROM Thuoc t WHERE t.keDon = false");
			thuocList = (ArrayList<Thuoc>) query.getResultList();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return thuocList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Thuoc> getallthuocExpired() throws RemoteException {
	    ArrayList<Thuoc> thuocList = new ArrayList<>();
	    try {
	        Query query = entityManager.createQuery("SELECT t FROM Thuoc t WHERE t.ngayHetHan < CURRENT_DATE");
	        thuocList = (ArrayList<Thuoc>) query.getResultList();
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return thuocList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Thuoc> getThuocByTenThuoc(String tenThuoc) throws RemoteException {
	    ArrayList<Thuoc> thuocList = new ArrayList<>();
	    try {
	        Query query = entityManager.createQuery("SELECT t FROM Thuoc t WHERE t.tenThuoc LIKE :tenThuoc");
	        query.setParameter("tenThuoc", "%" + tenThuoc + "%");
	        thuocList = (ArrayList<Thuoc>) query.getResultList();
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return thuocList;
	}

	@Override
	public Thuoc getThuocByMa(String ma) throws RemoteException {
	    try {
	        return entityManager.find(Thuoc.class, ma);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return null;
	}

	@Override
	public boolean deleteThuoc(String maThuoc) throws RemoteException {
	    EntityTransaction trans = entityManager.getTransaction();
	    try {
	        trans.begin();
	        Thuoc thuoc = entityManager.find(Thuoc.class, maThuoc);
	        if (thuoc != null) {
	            entityManager.remove(thuoc);
	            trans.commit();
	            return true;
	        }
	    } catch (Exception e) {
	        if (trans.isActive()) {
	            trans.rollback();
	        }
	        e.printStackTrace();
	    }
	    return false;
	}

	@Override
	public boolean addThuoc(Thuoc thuoc) throws RemoteException {
	    EntityTransaction trans = entityManager.getTransaction();
	    try {
	        trans.begin();
	        entityManager.persist(thuoc);
	        trans.commit();
	        return true;
	    } catch (Exception e) {
	        if (trans.isActive()) {
	            trans.rollback();
	        }
	        e.printStackTrace();
	        return false;
	    }
	}

	@Override
	public int countThuoc() throws RemoteException {
	    try {
	        Query query = entityManager.createQuery("SELECT COUNT(t) FROM Thuoc t");
	        return ((Number) query.getSingleResult()).intValue();
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return 0;
	}

	@Override
	public boolean updateSoLuongTon(String maThuoc, int newSoLuongTon) throws RemoteException {
	    EntityTransaction trans = entityManager.getTransaction();
	    try {
	        trans.begin();
	        Thuoc thuoc = entityManager.find(Thuoc.class, maThuoc);
	        if (thuoc != null) {
	            thuoc.setSoLuongTon(newSoLuongTon);
	            entityManager.merge(thuoc);
	            trans.commit();
	            return true;
	        }
	    } catch (Exception e) {
	        if (trans.isActive()) {
	            trans.rollback();
	        }
	        e.printStackTrace();
	    }
	    return false;
	}

	@Override
	public int getSoLuongTonByMaThuoc(String maThuoc) throws RemoteException {
	    try {
	        Thuoc thuoc = entityManager.find(Thuoc.class, maThuoc);
	        if (thuoc != null) {
	            return thuoc.getSoLuongTon();
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return 0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Thuoc> getallthuoc() throws RemoteException {
	    ArrayList<Thuoc> thuocList = new ArrayList<>();
	    try {
	        Query query = entityManager.createQuery("SELECT t FROM Thuoc t");
	        thuocList = (ArrayList<Thuoc>) query.getResultList();
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return thuocList;
	}

	@Override
	public String getanhthuoc(String ma) throws RemoteException {
	    String path = "";
	    try {
	        Query query = entityManager.createQuery("SELECT t.hinhThuoc FROM Thuoc t WHERE t.maThuoc = :ma");
	        query.setParameter("ma", ma);
	        path = (String) query.getSingleResult();
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return path;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<String> getalltenthuoc() throws RemoteException {
	    ArrayList<String> tenThuocList = new ArrayList<>();
	    try {
	        Query query = entityManager.createQuery("SELECT t.tenThuoc FROM Thuoc t");
	        tenThuocList = (ArrayList<String>) query.getResultList();
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return tenThuocList;
	}


}
