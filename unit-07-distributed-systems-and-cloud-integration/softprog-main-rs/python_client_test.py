import requests
import json
url = "http://localhost:8080/softprog-rs-1.0-SNAPSHOT/services/alumno"
payload = json.dumps({
  "nombre": "Jose2222",
  "apellido": "Corcuera2222"
})
headers = {
  'Content-Type': 'application/json'
}
response = requests.request("POST", url, headers=headers, data=payload)
print(response.text)
