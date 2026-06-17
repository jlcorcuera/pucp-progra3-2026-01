using Newtonsoft.Json;
using System;
using System.Buffers.Text;
using System.Collections.Generic;
using System.ComponentModel;
using System.Net;
using System.Text;

namespace CSharpRestClient
{
    public class HttpClientUtils<T>
    {
        public T get(string url)
        {
            HttpWebRequest req = (HttpWebRequest)WebRequest.Create($"{url}");
            req.Method = "GET";
            req.Accept = "application/json";
            req.Timeout = 30000;

            using (HttpWebResponse resp = (HttpWebResponse)req.GetResponse())
            using (StreamReader sr = new StreamReader(resp.GetResponseStream()))
            {
                string json = sr.ReadToEnd();
                T result = JsonConvert.DeserializeObject<T>(json);

                return result;
            }
        }

        public T post(string url, Object data)
        {
            HttpWebRequest req = (HttpWebRequest)WebRequest.Create($"{url}");
            req.Method = "POST";
            req.Accept = "application/json";
            req.ContentType = "application/json";
            req.Timeout = 30000;

            string jsonBody = JsonConvert.SerializeObject(data);

            using (StreamWriter sw = new StreamWriter(req.GetRequestStream()))
            {
                sw.Write(jsonBody);
                sw.Flush();
            }

            using (HttpWebResponse resp = (HttpWebResponse)req.GetResponse())
            using (StreamReader sr = new StreamReader(resp.GetResponseStream()))
            {
                string json = sr.ReadToEnd();
                T result = JsonConvert.DeserializeObject<T>(json);

                return result;
            }
        }

    }
}
