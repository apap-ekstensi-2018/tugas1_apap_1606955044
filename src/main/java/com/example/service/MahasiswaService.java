package com.example.service;

import java.util.List;

import com.example.model.FakultasModel;

import com.example.model.MahasiswaModel;
import com.example.model.ProdiModel;
import com.example.model.UniversitasModel;

public interface MahasiswaService {
	MahasiswaModel selectMahasiswa(String npm);
	String selectMahasiswaNPM(String npm);
	ProdiModel selectProdi (int id);
	FakultasModel selectFakultas (int id);
	UniversitasModel selectUniversitas(int id);
	
	
	
	
	List<MahasiswaModel> selectAllMahasiswa();
	List<FakultasModel> selectAllFakultas();
	List<ProdiModel> selectAllProdi();
	List<UniversitasModel> selectAllUniversitas();
	List<FakultasModel> selectAllFakultasbyUniv(int id_univ);
	List<ProdiModel> selectProdibyFak (int id_fakultas);
	List<MahasiswaModel> selectMahasiswaByProdi(int id_prodi);
	
	void tambahMahasiswa(MahasiswaModel mahasiswa);
	void updateMahasiswa(MahasiswaModel mahasiswa);
	int presentasebyAll(String tahun_masuk, int id_prodi);
	int presentasebyStatus( String tahun_masuk, int id_prodi);
	int selectUniv (int id);
	int selectFak(int id);
	
	
	
}
