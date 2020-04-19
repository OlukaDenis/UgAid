package com.app.ugaid.data.local;

import android.util.Log;

import androidx.work.ListenableWorker;

import com.app.ugaid.data.api.ApiClient;
import com.app.ugaid.data.api.ApiService;
import com.app.ugaid.data.api.StatesApiClient;
import com.app.ugaid.model.Covid;
import com.app.ugaid.model.Hospital;
import com.app.ugaid.model.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class LocalDataSource {
    private static final String TAG = "LocalDataSource";

    public LocalDataSource() {
    }

    public static List<String> ALL_STATES = new ArrayList<>();

    public static List<String> hospitals(){

        List<String> hList = new ArrayList<>();

        hList.add("Armed Forces Hospital");
        hList.add( "Abia State University Teaching Hospital");
        hList.add( "Abubakar Tafawa Balewa University Teaching Hospital");
        hList.add( "Abuja Clinics");
        hList.add( "Ahmadu Bello University Teaching Hospital");
        hList.add( "Aminu Kano Teaching Hospital");
        hList.add("Anambra State University Teaching Hospital");
        hList.add( "Babcock University Teaching Hospital");
        hList.add( "Baptist Medical Centre");
        hList.add("Barau Dikko Specialist Hospital");
        hList.add("Benue State University Teaching Hospital ");
        hList.add(" Bingham University Teaching Hospital  ");
        hList.add("Braithewaite Memorial Hospital");
        hList.add("Central Hospital");
        hList.add("Dalhatu Araf Hospital");
        hList.add("Delta State University Teaching Hospital");
        hList.add("Dental Centre Complex Dugbe");
        hList.add("Duro Soleye Hospital");
        hList.add("Ebonyi State University Teaching Hospital");
        hList.add("Eko Hospital Plc");
        hList.add(" Federal Medical Centre Abeokuta");
        hList.add("Federal Medical Centre Asaba");
        hList.add("Federal Medical Centre Ebute-Metta ");
        hList.add("Federal Medical Centre Keffi ");
        hList.add("Federal Medical Centre Lokoja");
        hList.add("Federal Medical Centre Bida");
        hList.add("Federal Medical Centre Makurdi");
        hList.add(" Federal Medical Centre Owerri");
        hList.add("Federal Medical Centre Owo");
        hList.add("Federal Medical Centre Umuahia");
        hList.add(" Federal Medical Centre Yenagoa");
        hList.add("Federal Teaching Hospital Abakaliki");
        hList.add("Federal Teaching Hospital Ido-Ekiti");
        hList.add("Federal Teaching Hospital, Gombe");
        hList.add("First Consultant medical centre Obalande");
        hList.add("Garki Hospital");
        hList.add(" General Hospital Onitsha");
        hList.add("General Hospital, Ikot-Ekpene");
        hList.add("General Hospital, Minna");
        hList.add("General Hospital Island Maternity Massey Children\u2019s Hospital");
        hList.add("Havanah Specialist Hospital Ltd");
        hList.add(" Holy Rosary Hospital");
        hList.add("Igbinedion University Teaching Hospital");
        hList.add("Immanuel General Hospital");
        hList.add("Imo State University Teaching Hospital");
        hList.add("Jos University Teaching Hospital");
        hList.add("Lagos State University Teaching Hospital");
        hList.add("Lagos University Teaching Hospital,");
        hList.add("LAUTECH Teaching Hospital");
        hList.add(" Lily Hospital Warri");
        hList.add("Madonna University Teaching Hospital");
        hList.add("Military Base Hospital");
        hList.add("Motayo Hospital");
        hList.add(" NAF Hospita");
        hList.add("National Hospital");
        hList.add("Naval Hospital");
        hList.add("Niger Delta University Teaching Hospital");
        hList.add("Nnamdi Azikiwe University Teaching Hospital");
        hList.add(" OAU Teaching Hospital Complex");
        hList.add("Olabisi Onabanjo (Ogun State) University Teaching Hospital");
        hList.add("Oriafor Medical Centre Uromi");
        hList.add("Parklane General Hospital");
        hList.add("Plateau Hospital Jos ");
        hList.add("Police Hospital Falomo");
        hList.add("Ring Road Specialist Hospital Complex,");
        hList.add("Seventh-day Adventist Hospital");
        hList.add(" Sobi Specialist Hospital");
        hList.add("Luke Hospital Anua ");
        hList.add("Nicholas Hospital");
        hList.add("State Hospital Asubiaro");
        hList.add(" State hospital Ijebu-Ode ");
        hList.add("State Hospital, Abeokuta ");
        hList.add(" State Hospital, Akure");
        hList.add("State House Medical Centre Abuja ");
        hList.add("State specialist hospital Ikere-Ekiti");
        hList.add(" University College Hospital");
        hList.add(" University of Abuja Teaching Hospital");
        hList.add("University of Benin Teaching Hospital");
        hList.add(" University of Calabar Teaching Hospital");
        hList.add("University of Ilorin Teaching Hospital");
        hList.add("University of Maiduguri Teaching Hospital");
        hList.add("University of Nigeria Teaching Hospital");
        hList.add("University of Port Harcourt Teaching Hospital");
        hList.add(" University of Uyo Teaching Hospital");
        hList.add(" University Teaching Hospital, Ado-Eki");
        hList.add("Usmanu Danfodio University Teaching Hospital");

        return hList;
    }

    public static List<String> testingCenters(){
        List<String> hospitals = new ArrayList<>();

        hospitals.add("Mulago Referral Hospital");
        hospitals.add("Entebbe General Referral Hospital");

        return hospitals;
    }

    public static List<String> districts(){
        List<String> dist = new ArrayList<>();
        dist.add("Kampala");
        dist.add("Masaka");
        dist.add("Entebbe");
        dist.add("Entebbe");
        dist.add("Mukono");
        dist.add("Jinja");
        dist.add("Lugazi");
        dist.add("Wakiso");

        return dist;
    }


}
