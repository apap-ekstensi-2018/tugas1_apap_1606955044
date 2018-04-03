package com.example.controller;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.FakultasModel;
import com.example.model.MahasiswaModel;
import com.example.model.ProdiModel;

import com.example.model.UniversitasModel;
import com.example.service.MahasiswaService;




@Controller
public class MahasiswaController {
	@Autowired
	MahasiswaService mhsDAO;
	
	
	@RequestMapping("/")
	public String index ()
    {
        return "index";
    }
	
	 @RequestMapping("/mahasiswa{npm}")
	    public String view (Model model,
	            @RequestParam(value = "npm", required = false) String npm)
	    {
	        MahasiswaModel mahasiswa = mhsDAO.selectMahasiswa(npm);
	        ProdiModel prodi = mhsDAO.selectProdi(mahasiswa.getId_prodi());
	        FakultasModel fakultas = mhsDAO.selectFakultas(prodi.getId_fakultas());
	        UniversitasModel universitas = mhsDAO.selectUniversitas(fakultas.getId_univ());
	        
	        

	        if (mahasiswa != null) {
	            model.addAttribute ("mahasiswa", mahasiswa);
	            model.addAttribute ("prodi", prodi);
	            model.addAttribute ("fakultas", fakultas);
	            model.addAttribute ("universitas", universitas);
	            return "view";
	        } else {
	        		model.addAttribute ("npm", npm);
	            return "not-found";
	        }
	    }
	
	@RequestMapping("/mahasiswa/tambah")
	public String tambah (@ModelAttribute("mahasiswa") MahasiswaModel mahasiswa, Model model)
	{
		if(mahasiswa.getNama()==null) {
			return "addMahasiswa";
		} else {
			mahasiswa.setNpm(generateNpm(mahasiswa));
			mahasiswa.setStatus("Aktif");
			model.addAttribute("npm", mahasiswa.getNpm());
			model.addAttribute("message", "Mahasiswa dengan NPM " + mahasiswa.getNpm() + " berhasil ditambahkan");
			mhsDAO.tambahMahasiswa(mahasiswa);
			return "success-add";
		}
	}
	
	public String generateNpm(MahasiswaModel mahasiswa) {
		ProdiModel prodi = mhsDAO.selectProdi(mahasiswa.getId_prodi());
		FakultasModel fakultas = mhsDAO.selectFakultas(prodi.getId_fakultas());
		UniversitasModel universitas = mhsDAO.selectUniversitas(fakultas.getId_univ());
		
		String duadigit  = mahasiswa.getTahun_masuk().substring(2, 4);
		String upDigit = universitas.getKode_univ()+prodi.getKode_prodi();
		String tigadigit="";
		    	if (mahasiswa.getJalur_masuk().equals("Undangan Olimpiade")) {
		    		tigadigit = "53";
		    	}
		    	else if(mahasiswa.getJalur_masuk().equals("Undangan Reguler/SNMPTN")) {
		    		tigadigit="54";
		    	}
		    	else if (mahasiswa.getJalur_masuk().equals("Undangan Paralel/PPKB")) {
		    		tigadigit="55";
		    	}
		    	else if (mahasiswa.getJalur_masuk().equals("Ujian Tulis Mandiri")) {
		    		tigadigit="62";
		    	}
		    	else if (mahasiswa.getJalur_masuk().equals("Ujian Tulis Bersama/SBMPTN")) {
		    		tigadigit="57";
		    	}
		    	String npmFiks = duadigit + upDigit + tigadigit;
		    	String cekNpm = mhsDAO.selectMahasiswaNPM(npmFiks);
		    	if(cekNpm != null) {
		    	cekNpm = "" + (Integer.parseInt(cekNpm) + 1);
		    	if (cekNpm.length() == 1) {
    			npmFiks = npmFiks + "00" + cekNpm;
    		}else if (cekNpm.length() == 2) {
    			npmFiks = npmFiks + "0" + cekNpm;
    		}else {
    			npmFiks = npmFiks + cekNpm;
    		}
    	}else {
    		npmFiks = npmFiks + "001";
    	}
		    	return npmFiks;
	}
	
	@RequestMapping("/mahasiswa/update/{npm}")
    public String update (@PathVariable(value = "npm") String npm, Model model, 
    		@ModelAttribute("mahasiswa") MahasiswaModel newmahasiswa)
    {
    		MahasiswaModel mahasiswa = mhsDAO.selectMahasiswa(npm);
    		
    		if(newmahasiswa.getNama()==null) {
    			if(mahasiswa==null) {
    				model.addAttribute("npm", npm);
    				return "not-found";
    			}
    			model.addAttribute("mahasiswa", mahasiswa);
    			model.addAttribute("title", "Update Mahasiswa");
    			return "updateMahasiswa";
    		} else {
    			if(!mahasiswa.getTahun_masuk().equals(newmahasiswa.getTahun_masuk()) || 
    					mahasiswa.getId_prodi() != newmahasiswa.getId_prodi() || 
    					!mahasiswa.getJalur_masuk().equals(newmahasiswa.getJalur_masuk())) {
    				newmahasiswa.setNpm(generateNpm(newmahasiswa));

        			mhsDAO.updateMahasiswa(newmahasiswa);
        			model.addAttribute("npm", mahasiswa.getNpm());
        			model.addAttribute("title", "Update Mahasiswa");
        			return "success-update";
    			}else {
    				newmahasiswa.setNpm(mahasiswa.getNpm());

        			mhsDAO.updateMahasiswa(newmahasiswa);
        			model.addAttribute("npm", mahasiswa.getNpm());
        			model.addAttribute("title", "Update Mahasiswa");
        			return "success-update";
    			}
    			
    			
    		}
    	}
	
	
	@RequestMapping("/kelulusan")
    public String kelulusan(Model model,
    						@RequestParam(value = "tahun_masuk", required = false) Optional<String> tahun_masuk,
    						@RequestParam(value = "prodi", required = false) Optional<String> prodi)
{
	if (tahun_masuk.isPresent() && prodi.isPresent()) {
		int jmlMahasiswa = mhsDAO.presentasebyAll(tahun_masuk.get(), Integer.parseInt(prodi.get()));
		int mahasiswaLulus = mhsDAO.presentasebyStatus(tahun_masuk.get(), Integer.parseInt(prodi.get()));
		double present = ((double) mahasiswaLulus / (double) jmlMahasiswa) * 100;
		String presentase = new DecimalFormat("##.##").format(present);
		ProdiModel program_studi = mhsDAO.selectProdi(Integer.parseInt(prodi.get()));
		FakultasModel fakultas = mhsDAO.selectFakultas(program_studi.getId_fakultas());
		UniversitasModel universitas = mhsDAO.selectUniversitas(fakultas.getId_univ());
			model.addAttribute("jmlMahasiswa", jmlMahasiswa);
			model.addAttribute("jmlMahasiswaLulus", mahasiswaLulus);
			model.addAttribute("presentase", presentase);
			model.addAttribute("tahun_masuk", tahun_masuk.get());
			model.addAttribute("prodi", program_studi.getNama_prodi());
			model.addAttribute("fakultas", fakultas.getNama_fakultas());
			model.addAttribute("universitas", universitas.getNama_univ());
			
		return "lihat-lulus";
		
	}else {
		List<ProdiModel> programStudi = mhsDAO.selectAllProdi();
		model.addAttribute("programStudi", programStudi);
		return "form-lulus";
	}
} 
	@RequestMapping("/mahasiswa/cari")
	public String cariMahasiswa(Model model,
			@RequestParam(value = "universitas", required = false) String univ,
			@RequestParam(value = "idfakultas", required = false) String idfakultas,
			@RequestParam(value = "idprodi", required = false) String idprodi)
	{

			List<UniversitasModel> universitas = mhsDAO.selectAllUniversitas();
			model.addAttribute ("universitas", universitas);
            	if(univ== null) {
            		return "cari-universitas";
            	} else {
            		int idUniv = Integer.parseInt(univ);
            		UniversitasModel univers = mhsDAO.selectUniversitas(idUniv);
            		int idUnivv = mhsDAO.selectUniv(idUniv);
            		List<FakultasModel> fakultas = mhsDAO.selectAllFakultasbyUniv(idUnivv);
            		if (idfakultas == null) {
            			model.addAttribute("fakultas", fakultas);
            			model.addAttribute("selectUniv",idUniv);
                		return "cari-fakultas";
            		}  
            		else {
            			int idFakul = Integer.parseInt(idfakultas);
            			FakultasModel fakultass = mhsDAO.selectFakultas(idFakul);
            			int idFaks = mhsDAO.selectFak(idFakul);
            			model.addAttribute("selectFak", idFakul);
            			List<ProdiModel> prodd = mhsDAO.selectProdibyFak(idFaks);
            			
            			if(idprodi == null) {
            				model.addAttribute("fakultas", fakultas);
                			model.addAttribute("selectUniv",idUniv);
                			model.addAttribute("prodi",prodd);
                			
                    		return "cari-prodi";
            			}else {
            				int idprod = Integer.parseInt(idprodi);
            				ProdiModel prodis = mhsDAO.selectProdi(idprod);
            				List<MahasiswaModel> mahasiswaByProdi = mhsDAO.selectMahasiswaByProdi(idprod);
            				model.addAttribute("mahasiswaByProdi", mahasiswaByProdi);
            				ProdiModel prodiSelectObject = mhsDAO.selectProdi(idprod);
            				model.addAttribute("prodiSelectObject", prodiSelectObject);
            				
            				return "tabelMahasiswa";
            				
            			}
            			
            		}
            		
            	}
            
	        
	}
    
}
	
	



