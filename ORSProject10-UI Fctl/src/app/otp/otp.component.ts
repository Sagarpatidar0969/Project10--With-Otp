import { Component, OnInit } from '@angular/core';
import { HttpServiceService } from '../http-service.service';
import { DataValidator } from '../utility/data-validator';
import { CookieService } from 'ngx-cookie-service';
import { ActivatedRoute, Router } from '@angular/router';
import { ServiceLocatorService } from '../service-locator.service';

@Component({
  selector: 'app-otp',
  templateUrl: './otp.component.html',
  styleUrls: ['./otp.component.css']
})
export class OtpComponent implements OnInit {

  endpoint = "http://localhost:8084/otp";



  form = {
    error: false,
    message: '',
    otp: '',
    
  };

 

  constructor(private httpService: HttpServiceService, private dataValidator: DataValidator, private router: Router,
    private cookieService: CookieService, private route: ActivatedRoute, private serviceLocator: ServiceLocatorService) {

  }

  ngOnInit() {
  }


  validate(){
    let flag = true;
    flag = flag && this.dataValidator.isNotNull(this.form.otp);
    return flag;
  }



  submit(){

    var self = this;

    this.httpService.post(this.endpoint + "/otpAuth", this.form, function (res) {
  
     

      if (self.dataValidator.isNotNullObject(res.result.message)) {
        self.form.message = res.result.message;
      }


      if(self.dataValidator.isTrue(res.success)){

        console.log('otp valid===================================')

       localStorage.setItem("otp", res.result.otp);



      

     const requestedUrl =localStorage.getItem('url');

      console.log("opt get================",requestedUrl)
  

       if (requestedUrl != null && requestedUrl != '') {

        self.router.navigateByUrl(requestedUrl);

      } else {
        self.router.navigateByUrl('dashboard');
      }
       
    } 

     
      
});
}

}
