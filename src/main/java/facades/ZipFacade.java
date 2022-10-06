package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ZipDto;
import entities.CityInfo;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;

public class ZipFacade {

    private static EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();

    public static void main(String[] args) throws IOException, InterruptedException {
        Gson GSON = new GsonBuilder().create();
        String urlString = "https://api.dataforsyningen.dk/postnumre";
        URL zipCode = new URL(urlString);
        URLConnection yc = zipCode.openConnection();
        BufferedReader in = new BufferedReader( new InputStreamReader(yc.getInputStream()));
        String inputLine;
        StringBuilder sb = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
            sb.append(inputLine);
        in.close();

        EntityManager em = emf.createEntityManager();

        try {
            ZipDto[] zipDtos = GSON.fromJson(sb.toString(), ZipDto[].class);
            em.getTransaction().begin();
            for (ZipDto zipDto : zipDtos) {
                em.persist(new CityInfo(zipDto.getNr(), zipDto.getNavn()));
                System.out.println(zipDto.getNr() + " " + zipDto.getNavn());
            }
            em.getTransaction().commit();
        } finally {
        em.close();
        }
    }

}
