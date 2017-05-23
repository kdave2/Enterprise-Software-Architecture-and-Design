module namespace sol = "http://www.example.org/schemas/solutions";

import schema namespace c = "http://www.example.org/schemas/clinic" at "Clinic.xsd";

import schema namespace p = "http://www.example.org/schemas/clinic/patient" at "Patient.xsd";

import schema namespace t = "http://www.example.org/schemas/clinic/treatment" at "Treatment.xsd";

import schema namespace pr = "http://www.example.org/schemas/clinic/provider" at "Provider.xsd";

declare namespace ps = "http://www.example.org/schemas/clinic/patients";



(:Solution 1:)

declare function sol:getPatientTreatments ($p as xs:decimal?, $klinic as element(c:Clinic))
as element(li, xs:untyped)*
{
    for $s in $klinic/p:patient
        where $s/p:patient-id = $p
    return 
<li>
{
    for $tr in $s/p:treatments/p:treatment
    return
        <li>
        Provider Id : {data($tr/t:provider-id)}
        Diagnosis : {data($tr/t:diagnosis)}
        {
            if ($tr/t:drug-treatment) then
                <li>
                Drug Treatment
                Drug Name : {data($tr/t:drug-treatment/t:name)}
                Drug Dosage : {data($tr/t:drug-treatment/t:dosage)}
                </li>
            else if ($tr/t:radiology) then
                <li>
                Radiology
                Date : {data($tr/t:radiology/t:date)}
            </li>
            else if ($tr/t:surgery) then
                <li>
                Surgery
                Date : {data($tr/t:surgery/t:date)}
                </li>
            else ()
            }
    </li>
}

</li>
};



(:Solution 2:)


declare function sol:getPatientDrugs ($p as xs:decimal?, $klinic as element(c:Clinic))
as element(li, xs:untyped)*
{
        for $s in $klinic/p:patient
        where $s/p:patient-id = $p
        return <li>
        {
            for $name1 in $s/p:treatments/p:treatment
            return if ($name1/t:drug-treatment) then
                    <li> Drug Name: {data($name1/t:drug-treatment/t:name)} 
                        Drug Dosage: {data($name1/t:drug-treatment/t:dosage)}
                        Diagnosis: { data($name1/t:diagnosis)}
                    </li>
                    else ()
        }
       </li>
};



(:Solution 3:)

declare function sol:getTreatmentInfo ($klinic as element(c:Clinic))
as element(li, xs:untyped)*
{
    for $s in $klinic/p:patient
    return
    <li>
    {
        for $tr in $s/p:treatments/p:treatment
        return

        if ($tr/t:drug-treatment) then
        <li>
                Drug Treatment
                
                Patient Id: {data($s/p:patient-id)}
                Patient Name: {data($s/p:name)}
                {
                     for $pr in $klinic/pr:provider
                     where $pr/pr:provider-id = $tr/t:provider-id
                     return 
                        <li>
                        Provider Id: {data($pr/pr:provider-id)}
                        Provider Name: {data($pr/pr:name)}
                        </li>
        }
        </li>
        else if ($tr/t:radiology) then
        <li>
                  Radiology        
                  
                  Patient Id: {data($s/p:patient-id)}
                  Patient Name: {data($s/p:name)}
                  {
                       for $pr in $klinic/pr:provider
                       where $pr/pr:provider-id = $tr/t:provider-id
                       return 
                          <li>
                          Provider Id: {data($pr/pr:provider-id)}
                          Provider Name: {data($pr/pr:name)}
                          </li>
        }
        </li>
        else if ($tr/t:surgery) then
        <li>
                  Surgery
                  
                  Patient Id: {data($s/p:patient-id)}
                  Patient Name: {data($s/p:name)}
                  {
                       for $pr in $klinic/pr:provider
                       where $pr/pr:provider-id = $tr/t:provider-id
                       return 
                       <li>
                          Provider Id: {data($pr/pr:provider-id)}
                          Provider Name: {data($pr/pr:name)}
                       </li>
                        }
        </li>
        else()
    }
    </li>

};



(:Solution 4:)

declare function sol:getProviderInfo ($klinic as element(c:Clinic))
as element(li, xs:untyped)*
{
for $pr in $klinic/pr:provider   
return 
<li>
{
        <li>
            Provider Id: {data($pr/pr:provider-id)}
            Provider Name: {data($pr/pr:name)}
       </li>
       }
       {
       for $s in $klinic/p:patient
            where $s/p:treatments/p:treatment/t:provider-id = $pr/pr:provider-id
            return <li>
                   {
                    <li>
                        Patient ID: {data($s/p:patient-id)}
                        Patient Name : {data($s/p:name)}
                   </li>
                    }
                    {
                         for $trmt in $s/p:treatments/p:treatment
                         return
                            if ($trmt/t:drug-treatment and $trmt/t:provider-id = $pr/pr:provider-id) then
                                    <li>           Drug Treatment</li>
                            else if ($trmt/t:radiology and $trmt/t:provider-id = $pr/pr:provider-id) then
                                    <li>           RadioLogy</li>
                            else if ($trmt/t:surgery and $trmt/t:provider-id = $pr/pr:provider-id) then
                                    <li>           Surgery</li>
                            else ()
                    }
        </li>
        }
        
        </li>
};



(:Solution 5:)

declare function sol:getDrugInfo ($klinic as element(c:Clinic))
as element(li, xs:untyped)*
{
    for $dt in distinct-values($klinic/p:patient/p:treatments/p:treatment/t:drug-treatment/t:name)
    return <li>
        {
            <li>
                Drug Name: {data($dt)}
                {
                for $pt in $klinic/p:patient
                where $pt/p:treatments/p:treatment/t:drug-treatment/t:name = $dt
                return
                <li>
                    Patient Id: {data($pt/p:patient-id)}
                    Patient Name: {data($pt/p:name)}
                    {
                    for $treat in $pt/p:treatments/p:treatment
                    where $treat/t:drug-treatment/t:name = $dt
                    return 
                    <li>
                    Diagnosis: {data($treat/t:diagnosis)}
                    {
                    for $prov in $klinic/pr:provider
                    where $prov/pr:provider-id = $treat/t:provider-id
                    return 
                    <li>
                    Provider Id: {data($prov/pr:provider-id)}
                    Provider Name: {data($prov/pr:name)}
                    </li>
                    }
                    </li>
                    }                    
                </li>           
                }
            </li>
        }
        </li>
};