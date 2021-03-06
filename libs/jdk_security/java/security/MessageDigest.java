/* MessageDigest.java --- The message digest interface.
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


package java.security;

public abstract class MessageDigest extends MessageDigestSpi
{
  private String algorithm;
  private Provider provider;
  private byte[] lastDigest;

  /**
     Creates a MessageDigest representing the specified
     algorithm.

     @param algorithm the name of digest algorithm to choose
  */
  protected MessageDigest (String algorithm)
  {
    this.algorithm = algorithm;
    provider = null;
  }

  /** 
      Gets an instance of the MessageDigest class representing
      the specified digest. If the algorithm is not found then, 
      it throws NoSuchAlgorithmException.

      @param algorithm the name of digest algorithm to choose
      @return a MessageDigest representing the desired algorithm

      @exception NoSuchAlgorithmException if the algorithm is not implemented by providers
  */
  public static MessageDigest getInstance (String algorithm)
    throws NoSuchAlgorithmException
  {
    Provider[] p = Security.getProviders ();

    for (int i = 0; i < p.length; i++)
      {
	String classname = p[i].getProperty ("MessageDigest." + algorithm);
	if (classname != null)
	  return getInstance (classname, algorithm, p[i]);
      }

    throw new NoSuchAlgorithmException (algorithm);
  }

  /** 
      Gets an instance of the MessageDigest class representing
      the specified digest from the specified provider. If the 
      algorithm is not found then, it throws NoSuchAlgorithmException.
      If the provider is not found, then it throws
      NoSuchProviderException.

      @param algorithm the name of digest algorithm to choose
      @param provider the name of the provider to find the algorithm in
      @return a MessageDigest representing the desired algorithm

      @exception NoSuchAlgorithmException if the algorithm is not implemented by the provider
      @exception NoSuchProviderException if the provider is not found
  */

  public static MessageDigest getInstance (String algorithm, String provider)
    throws NoSuchAlgorithmException, NoSuchProviderException
  {
    Provider p = Security.getProvider (provider);

    if (p == null)
      throw new NoSuchProviderException (provider);

    return getInstance (p.getProperty ("MessageDigest." + algorithm),
			algorithm, p);
  }

  private static MessageDigest getInstance (String classname,
					    String algorithm,
					    Provider provider)
    throws NoSuchAlgorithmException
  {
    if (classname == null)
      throw new NoSuchAlgorithmException (algorithm);

    try
      {
	MessageDigest m = (MessageDigest)Class.forName (classname).newInstance();
	m.algorithm = algorithm;
	m.provider = provider;
	return m;		
      }
    catch (ClassNotFoundException cnfe)
      {
	throw new NoSuchAlgorithmException (algorithm + ": Class not found.");
      }
    catch (InstantiationException ie)
      {
	throw new NoSuchAlgorithmException (algorithm
					    + ": Class instantiation failed.");
      }
    catch (IllegalAccessException iae)
      {
	throw new NoSuchAlgorithmException (algorithm + ": Illegal Access");
      }
  }
  
  
  /**
     Gets the provider that the MessageDigest is from.
     
     @return the provider the this MessageDigest
  */
  public final Provider getProvider ()
  {
    return provider;
  }

  /**
     Updates the digest with the byte.
     
     @param input byte to update the digest with
  */
  public void update (byte input)
  {
    engineUpdate (input);
  }

  /**
     Updates the digest with the bytes from the array from the
     specified offset to the specified length.

     @param input bytes to update the digest with
     @param offset the offset to start at
     @param len length of the data to update with
  */
  public void update (byte[] input, int offset, int len)
  {
    engineUpdate (input, 0, input.length);
  }

  /**
     Updates the digest with the bytes from the array.

     @param input bytes to update the digest with
  */
  public void update (byte[] input)
  {
    engineUpdate (input, 0, input.length);
  }

  /**
     Computes the digest of the stored data.

     @return a byte array representing the message digest
  */
  public byte[] digest ()
  {
    return lastDigest = engineDigest ();
  }

  /**
     Computes the final digest of the stored bytes and returns
     them. 

     @param buf An array of bytes to store the digest
     @param offset An offset to start storing the digest at
     @param len The length of the buffer
     @return Returns the length of the buffer
  */
  public int digest(byte[] buf, int offset, int len) throws DigestException
  {
    return engineDigest( buf, offset, len);
  }

  /**
     Computes a final update using the input array of bytes,
     then computes a final digest and returns it. It calls 
     update(input) and then digest();

     @param buf An array of bytes to perform final update with
     @return a byte array representing the message digest
  */ 
  public byte[] digest (byte[] input)
  {
    update (input);
    return digest ();
  }

  /**
     Returns a representation of the MessageDigest as a String.

     @return a string representing the message digest
  */
  public String toString()
  {
    return (getClass ()).getName ()
      + " Message Digest <" + digestToString () + ">";
  }

  /**
     Does a simple byte comparison of the two digests.
     
     @param digesta first digest to compare
     @param digestb second digest to compare
     @return true if they are equal, false otherwise
  */
  public static boolean isEqual (byte[] digesta, byte[] digestb)
  {
    if (digesta.length != digestb.length)
      return false;

    for (int i = digesta.length - 1; i >= 0; -- i)
      if (digesta[i] != digestb[i])
	return false;

    return true;
  }


  /**
     Resets the message digest.
  */
  public void reset()
  {
    engineReset();
  }

  /** 
      Gets the name of the algorithm currently used.
      The names of algorithms are usually SHA-1 or MD5.

      @return name of algorithm.
  */
  public final String getAlgorithm ()
  {
    return algorithm;
  }

  /**
     Gets the length of the message digest.
     The default is zero which means that this message digest
     does not implement this function.

     @return length of the message digest
  */	
  public final int getDigestLength ()
  {
    return engineGetDigestLength ();
  }

  /**
     Returns a clone of this class if supported.
     If it does not then it throws CloneNotSupportedException.
     The cloning of this class depends on whether the subclass
     MessageDigestSpi implements Cloneable which contains the
     actual implementation of the appropriate algorithm.
     
     @return clone of this class
     
     @exception CloneNotSupportedException this class does not support cloning
  */
  public Object clone () throws CloneNotSupportedException
  {
    if (this instanceof Cloneable)
      return super.clone ();
    else
      throw new CloneNotSupportedException ();
  }

  private String digestToString ()
  {
    byte[] digest = lastDigest;

    if (digest == null)
      return "incomplete";

    StringBuffer buf = new StringBuffer ();
    int len = digest.length;
    for (int i = 0; i < len; ++ i)
      {
	byte b = digest[i];
	byte high = (byte)((b & 0xff) >>> 4);
	byte low = (byte)(b & 0xf);

	buf.append (high > 9 ? ('a' - 10) + high : '0' + high);
	buf.append (low > 9 ? ('a' - 10) + low : '0' + low);
      }

    return buf.toString ();
  }

}
