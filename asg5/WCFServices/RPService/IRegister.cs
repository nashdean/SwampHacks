using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace RegistrationService
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IRegister" in both code and config file together.
    [ServiceContract]
    public interface IRegister
    {
        [OperationContract]
        int Register(int StuNo, String CourseNo, String SecNo);

        [OperationContract]
        List<String> ListRegistered(int StuNo, String semester, String year);

        [OperationContract]
        List<String> ListAvailable(String semester, String year);




        // TODO: Add your service operations here
    }


    // Use a data contract as illustrated in the sample below to add composite types to service operations.
   
}
