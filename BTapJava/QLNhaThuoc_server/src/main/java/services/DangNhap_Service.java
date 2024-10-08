package services;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import dao.DangNhapDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

public class DangNhap_Service extends UnicastRemoteObject implements DangNhapDAO {
	private static final long serialVersionUID = 1L;
	private EntityManager entityManager;

	public DangNhap_Service(EntityManager entityManager) throws RemoteException {
		this.entityManager = entityManager;
	}

	@Override
	public boolean dNhap(String username, String password, boolean isAdmin) throws RemoteException {
		try {
			String queryStr;
			if (isAdmin) {
				queryStr = "SELECT COUNT(*) FROM TaiKhoanQuanLy WHERE taiKhoan = :username AND matKhau = :password";
			} else {
				queryStr = "SELECT COUNT(*) FROM TaiKhoanNhanVien WHERE maNV = :username AND matKhau = :password";
			}
			Query query = entityManager.createQuery(queryStr);
			query.setParameter("username", username);
			query.setParameter("password", password);
			Long count = (Long) query.getSingleResult();
			return count > 0;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
}
