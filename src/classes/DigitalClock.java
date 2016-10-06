/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.util.Duration;
/**
 *
 * @author Ricardo
 */
public class DigitalClock extends Label {
  
      Label date;
      Label hour;
    public DigitalClock(Label date, Label hour) {
          this.date=date;
          this.hour= hour;
    bindToTime();
  }

  // the digital clock updates once a second.
  private void bindToTime() {
    Timeline timeline = new Timeline(
      new KeyFrame(Duration.seconds(0),
        new EventHandler<ActionEvent>() {
          @Override public void handle(ActionEvent actionEvent) {
            Calendar time = Calendar.getInstance();
            String hourString = StringUtilities.pad(2, ' ', time.get(Calendar.HOUR) == 0 ? "12" : time.get(Calendar.HOUR) + "");
            String minuteString = StringUtilities.pad(2, '0', time.get(Calendar.MINUTE) + "");
            String secondString = StringUtilities.pad(2, '0', time.get(Calendar.SECOND) + "");
            String ampmString = time.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM"; 
            String[] days = new DateFormatSymbols(Locale.getDefault()).getWeekdays();
            String[] monts = new DateFormatSymbols(Locale.getDefault()).getMonths();
            date.setText(days[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)].toUpperCase()+" "+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+" DE "+monts[Calendar.getInstance().get(Calendar.MONTH)].toUpperCase());
            hour.setText(hourString + ":" + minuteString + ":" + secondString + " " + ampmString);
          }
        }
      ),
      new KeyFrame(Duration.seconds(1))
    );
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }
}