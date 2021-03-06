/* CRL.java --- Certificate Revocation List
   Copyright (C) 1999 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.
 
GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


package java.security.cert;

/**
   Certificate Revocation List class for managing CRLs that
   have different formats but the same general use. They
   all serve as lists of revoked certificates and can
   be queried for a given certificate.
   
   Specialized CRLs extend this class.
   
   @author Mark Benvenuto
   
   @since JDK 1.2
*/
public abstract class CRL
{

  private String type;

  /**
     Creates a new CRL for the specified type. An example
     is "X.509".

     @param type the standard name for the CRL type. 
  */
  protected CRL(String type)
  {
    this.type = type;
  }

  /**
     Returns the CRL type.

     @return a string representing the CRL type
  */
  public final String getType()
  {
    return type;
  }

  /**
     Returns a string representing the CRL.

     @return a string representing the CRL.
  */
  public abstract String toString();

  /**
     Determines whether or not the specified Certificate
     is revoked.

     @param cert A certificate to check if it is revoked

     @return true if the certificate is revoked,
     false otherwise.	
  */
  public abstract boolean isRevoked(Certificate cert);


}
