/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

public class string {
  private String s;
  private String t;

  public string( String s, String t ) {
    setInternalString( s );
    setInternalTag( t );
  }

  private void setInternalString( String s ) {
    this.s = s;
  }
  private void setInternalTag( String t ) {
    this.t = t;
  }

  private String getInternalString() {
    return this.s == null ? "" : this.s;
  }
  
  public String getTag() {
    return this.t == null ? "" : this.t;
  }
  
  @Override
  public String toString()
  {
      return s;
  }
}
