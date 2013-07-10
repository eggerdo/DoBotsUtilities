package org.dobots.utilities.cipher;

// http://www.schneier.com/blowfish-download.html
// Java version of Blowfish Cipher, adapted from C Source code
// requires blowfish.dat in asset folder to initialise P and S

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.dobots.utilities.R;

import android.content.res.Resources;

public class BlowFish {
	
	private static final int N = 16;

	private int[] P = new int[N + 2];
	private int[][] S = new int[4][256];

	private boolean bInitDataLoaded = false;
	
	public BlowFish(InputStream is) {
		loadInitData(is);
	}
	
	public BlowFish(int[] P, int[][] S) {
		loadInitData(P, S);
	}
	
	private int F(int x) {
		int a, b, c, d;
		int y;

		d = (short) (x & 0x00FF);
		x >>= 8;
		c = (short) (x & 0x00FF);
		x >>= 8;
		b = (short) (x & 0x00FF);
		x >>= 8;
		a = (short) (x & 0x00FF);

		y = S[0][a] + S[1][b];
		y = y ^ S[2][c];
		y = y + S[3][d];

		return y;
	}

	/**
	 * encipher the given two values
	 * @param left first value
	 * @param right second value
	 */
	public void encipher(int[] left, int[] right) {
		int Xl;
		int Xr;
		int temp;
		int i;

		Xl = left[0];
		Xr = right[0];

		for (i = 0; i < N; ++i) {
			Xl = Xl ^ P[i];
			Xr = F(Xl) ^ Xr;

			temp = Xl;
			Xl = Xr;
			Xr = temp;
		}

		temp = Xl;
		Xl = Xr;
		Xr = temp;

		Xr = Xr ^ P[N];
		Xl = Xl ^ P[N + 1];

		left[0] = Xl;
		right[0] = Xr;
	}

	/**
	 * decipher the given two encripted values
	 * @param left first value
	 * @param right second value
	 */
	public void decipher(int[] left, int[] right) {
		int Xl;
		int Xr;
		int temp;
		int i;

		Xl = left[0];
		Xr = right[0];

		for (i = N + 1; i > 1; --i) {
			Xl = Xl ^ P[i];
			Xr = F(Xl) ^ Xr;

			/* Exchange Xl and Xr */
			temp = Xl;
			Xl = Xr;
			Xr = temp;
		}

		/* Exchange Xl and Xr */
		temp = Xl;
		Xl = Xr;
		Xr = temp;

		Xr = Xr ^ P[1];
		Xl = Xl ^ P[0];

		left[0] = Xl;
		right[0] = Xr;
	}

	/**
	 * load initial values from input stream. first N values correspond to the P-array, after that follow the S-boxes
	 * @param is Input stream to read the values from
	 * @return 0 if successful, -1 otherwise
	 */
	private int loadInitData(InputStream is) {
		int				data;
		DataInputStream dis;
		int				i, j;

		/* First, open the file containing the array initialization data */
		dis = new DataInputStream(is);
		if (dis != null) {
			for (i = 0; i < N + 2; ++i) {
				try {
					data = dis.readInt();
					P[i] = data;
				} catch (IOException e) {
					e.printStackTrace();
					return -1;
				}
			}

			for (i = 0; i < 4; ++i) {
				for (j = 0; j < 256; ++j) {
					try {
						data = dis.readInt();
						S[i][j] = data;
					} catch (IOException e) {
						e.printStackTrace();
						return -1;
					}
				}
			}

			try {
				dis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return -1;
		}
		
		bInitDataLoaded = true;
		
		return 0;

	}
	
	/**
	 * use given arrays as initial values for the blowfish cypher subkey arrays
	 * @param P P-array
	 * @param S S-boxes
	 * @return 0 if successful, -1 otherwise
	 */
	private int loadInitData(int[] P, int[][] S) {

		this.P = P.clone();
		
		for (int i = 0; i < S.length; ++i) {
			this.S[i] = S[i].clone();
		}
		
		bInitDataLoaded = true;
		
		return 0;
	}
	
	/**
	 * Initialize blowfish
	 * @param key private key used to en/decipher
	 * @param keybytes length (in bytes) of the key
	 * @return 0 if successful, -1 otherwise
	 */
	public int initialize(byte key[], int keybytes)
	{
	    int		i;
		int		j;
		int		k;
		int		data;
		int[]	datal = new int[1];
		int[]	datar = new int[1];
		int 	result;

		if (!bInitDataLoaded) {
			return -1;
		}

		j = 0;
		for (i = 0; i < N + 2; ++i) {
			data = 0x00000000;
			for (k = 0; k < 4; ++k) {
				data = (data << 8) | key[j];
				j = j + 1;
				if (j >= keybytes) {
					j = 0;
				}
			}
			P[i] = P[i] ^ data;
		}

		datal[0] = 0x00000000;
		datar[0] = 0x00000000;

		for (i = 0; i < N + 2; i += 2) {
			encipher(datal, datar);

			P[i] = datal[0];
			P[i + 1] = datar[0];
		}

		for (i = 0; i < 4; ++i) {
			for (j = 0; j < 256; j += 2) {

				encipher(datal, datar);

				S[i][j] = datal[0];
				S[i][j + 1] = datar[0];
			}
		}

		return 0;
	}

}
