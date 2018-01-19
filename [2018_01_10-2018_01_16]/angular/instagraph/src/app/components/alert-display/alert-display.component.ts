import { Component, OnInit } from '@angular/core';
import { AlertManagerService } from '../../services/alert-manager.service';

@Component({
  selector: 'app-alert-display',
  templateUrl: './alert-display.component.html',
  styleUrls: ['./alert-display.component.css']
})
export class AlertDisplayComponent implements OnInit {
  
  public  alerts: any[] = [];

  constructor(private  alertManager: AlertManagerService) { }

  ngOnInit() {
    this.alertManager.alertsAsObservable().subscribe( alert => {
      this.alerts.push({
        type: alert[0],
        message: alert[1],
        timeout: 5000
      });
    });
  }

  public removeAlert(event) : void {
    //console.log(event);
    let pos = this.alerts.findIndex( a => a == event);
    if (pos >= 0) {
      //console.log("removing alert...");
      this.alerts.splice(pos, 1);
    }
  }

}
