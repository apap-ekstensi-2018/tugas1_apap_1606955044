package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.dao.MahasiswaMapper;
import com.example.model.FakultasModel;
import com.example.model.MahasiswaModel;
import com.example.model.ProdiModel;
import com.example.model.UniversitasModel;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class MahasiswaServiceDatabase implements MahasiswaService{
	@Autowired
	private MahasiswaMapper mahasiswaMapper;
	@Override
	public MahasiswaModel selectMahasiswa(String npm) {
		log.info ("select mahasiswa with npm {}",npm);
		return mahasiswaMapper.selectMahasiswa(npm);
	}

	@Override
	public List<MahasiswaModel> selectAllMahasiswa() {
		log.info ("select all mahasiswa");
        return mahasiswaMapper.selectAllMahasiswa();
	}

	@Override
	public ProdiModel selectProdi (int id) {
		log.info("select prodi");
		return mahasiswaMapper.selectProdi(id);
	}
	@Override
	public List<ProdiModel> selectAllProdi() {
		log.info ("select all program studi");
        return mahasiswaMapper.selectAllProdi();
	}

	@Override
	public FakultasModel selectFakultas(int id) {
		log.info("select fakultas");
		return mahasiswaMapper.selectFakultas(id);
	}
	@Override
	public List<FakultasModel> selectAllFakultas() {
		log.info ("select all fakultas");
        return mahasiswaMapper.selectAllFakultas();
	}

	@Override
	public UniversitasModel selectUniversitas(int id) {
		log.info("select universitas");
		return mahasiswaMapper.selectUniversitas(id);
	}

	@Override
	public List<UniversitasModel> selectAllUniversitas() {
		log.info("select all universitas");
		return mahasiswaMapper.selectAllUniversitas();
	}

	@Override
	public void tambahMahasiswa(MahasiswaModel mahasiswa) {
		log.info("add mahasiswa");
		 mahasiswaMapper.tambahMahasiswa(mahasiswa);
		
	}

	@Override
	public String selectMahasiswaNPM(String npm) {
		log.info("view mahasiswa by npm {}",npm);
		// TODO Auto-generated method stub	 
		return mahasiswaMapper.selectMahasiswaNPM(npm);
	}

	@Override
	public void updateMahasiswa(MahasiswaModel mahasiswa) {
		// TODO Auto-generated method stub
		log.info("update mahasiswa by npm");
		 mahasiswaMapper.updateMahasiswa(mahasiswa);
	}

	@Override
	public int presentasebyAll(String tahun_masuk,int id_prodi) {
		log.info("presentase mahasiswa by all");
		return mahasiswaMapper.presentasebyAll(tahun_masuk, id_prodi);
	}

	@Override
	public int presentasebyStatus( String tahun_masuk,int id_prodi) {
		log.info("presentase mahasiswa by status");
		return mahasiswaMapper.presentasebyStatus(tahun_masuk, id_prodi);
	}

	@Override
	public List<FakultasModel> selectAllFakultasbyUniv(int id_univ) {
		log.info("show by univ");
		return mahasiswaMapper.selectAllFakultasbyUniv(id_univ);
	}

	@Override
	public int selectUniv(int id) {
		log.info("show by univ");
		return mahasiswaMapper.selectUniv(id);
	}

	@Override
	public List<ProdiModel> selectProdibyFak(int id_fakultas) {
		log.info("show prodi");
		return mahasiswaMapper.selectProdibyFak(id_fakultas);
	}

	@Override
	public int selectFak(int id) {
		log.info("show prodi");
		return mahasiswaMapper.selectFak(id);
	}
	
	@Override
	public List<MahasiswaModel> selectMahasiswaByProdi(int id_prodi){
		log.info(" showmahasiswa ");
		return mahasiswaMapper.selectMahasiswaByProdi(id_prodi);
	}



}
