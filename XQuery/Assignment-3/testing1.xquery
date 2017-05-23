import module namespace sol = "http://www.example.org/schemas/solutions" at "solutions.xquery";

import schema namespace c = "http://www.example.org/schemas/clinic" at "Clinic.xsd";

let $clinic := doc("ClinicData.xml")/c:Clinic

return sol:getPatientTreatments(34564325,$clinic)