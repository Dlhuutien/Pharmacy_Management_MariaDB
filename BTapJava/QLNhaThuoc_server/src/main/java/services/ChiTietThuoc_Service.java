package services;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import dao.ChiTietThuocDAO;
import entities.ChiTietThuoc;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

public class ChiTietThuoc_Service extends UnicastRemoteObject implements ChiTietThuocDAO{
	private static final long serialVersionUID = 1L;
	private EntityManager entityManager;

	public ChiTietThuoc_Service(EntityManager entityManager) throws RemoteException {
		this.entityManager = entityManager;
	}

	@Override
	public ArrayList<ChiTietThuoc> getalltptheomathuoc(String mathuoc) throws RemoteException {
	    ArrayList<ChiTietThuoc> dstp = new ArrayList<>();
	    try {
	        // Tạo truy vấn JPQL để lấy các chi tiết thuốc theo mã thuốc
	        Query query = entityManager.createQuery("SELECT ct FROM ChiTietThuoc ct WHERE ct.thuoc.maThuoc = :mathuoc");
	        query.setParameter("mathuoc", mathuoc);
	        
	        // Lấy kết quả từ truy vấn
	        @SuppressWarnings("unchecked")
	        List<ChiTietThuoc> results = query.getResultList();
	        
	        // Lặp qua các kết quả và thêm vào danh sách kết quả
	        for (ChiTietThuoc ct : results) {
	            dstp.add(ct);
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return dstp;
	}
	
	@Override
	public ArrayList<String> getAllChiTietThuocIDs() throws RemoteException {
	    ArrayList<String> ids = new ArrayList<>();
	    try {
	        Query query = entityManager.createQuery("SELECT ct.maTP FROM ChiTietThuoc ct");
	        @SuppressWarnings("unchecked")
	        List<String> results = query.getResultList();
	        ids.addAll(results);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return ids;
	}

	public boolean addChiTietThuoc(ChiTietThuoc chiTietThuoc) throws RemoteException {
		EntityTransaction trans = entityManager.getTransaction();
		try {
			trans.begin();
			entityManager.persist(chiTietThuoc);
			trans.commit();
			return true;
		} catch (Exception e) {
			if (trans.isActive()) {
				trans.rollback();
			}
			e.printStackTrace();
		}
		return false;
	}


    @Override
    public int countChiTietThuoc() throws RemoteException {
        int count = 0;
        try {
            Query query = entityManager.createQuery("SELECT COUNT(ct) FROM ChiTietThuoc ct");
            count = ((Number) query.getSingleResult()).intValue();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return count;
    }

	@Override
	public boolean deleteChiTietThuocByMaThuoc(String maThuoc) throws RemoteException {
		EntityTransaction transaction = null;
		try {
			transaction = entityManager.getTransaction();
			transaction.begin();
			Query query = entityManager.createQuery("DELETE FROM ChiTietThuoc c WHERE c.maThuoc = :maThuoc");
			query.setParameter("maThuoc", maThuoc);
			int n = query.executeUpdate();
			transaction.commit();
			return n > 0;
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
			return false;
		}
	}
}
