/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

/**
 *
 * @author Ricardo
 */
public class Plan {
      String plan;
      int cant;

      public Plan(String plan, int cant) {
            this.plan = plan;
            this.cant = cant;
      }

      public String getPlan() {
            return plan;
      }

      public int getCant() {
            return cant;
      }

      public void setPlan(String plan) {
            this.plan = plan;
      }

      public void setCant(int cant) {
            this.cant = cant;
      }
      
      
}
