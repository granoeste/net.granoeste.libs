/*
 * Copyright (C) 2014 granoeste.net http://granoeste.net/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.granoeste.commons.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.content.res.AssetManager;

public class FileUtils {

	static public void copyAssetsFiles(Context context, String filepath, String datapath) {
//		Log.d(TAG, "copyAssetsFiles() filepath="+filepath);
		// アセットファイルのストリーム作成
		AssetManager as = context.getResources().getAssets();

		try {

			String[] list = as.list(filepath);
			final int N = list.length;
            for (int i = 0; i < N; i++) {
				copyAssetsFiles(context, filepath + "/" + list[i], datapath);
			}
			if (N == 0) {
				copyStreamToFile(as.open(filepath), datapath + "/" + filepath);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static public void copyStreamToFile(InputStream input, String dstFilePath) throws IOException {
//		Log.d(TAG, "copyStreamToFile() dstFilePath="+dstFilePath);
		File dstFile = new File(dstFilePath);

		// ディレクトリを作る.
		dstFile.getParentFile().mkdirs();

		// ファイルコピーのフェーズ
		FileOutputStream output = new FileOutputStream(dstFile);

		final int DEFAULT_BUFFER_SIZE = 1024 * 4;
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		input.close();
		output.close();
	}

	static public void copyStreamToFile(InputStream input, File dstFile) throws IOException {
		// ディレクトリを作る.
		dstFile.getParentFile().mkdirs();

		// ファイルコピーのフェーズ
		FileOutputStream output = new FileOutputStream(dstFile);

		final int DEFAULT_BUFFER_SIZE = 1024 * 4;
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		input.close();
		output.close();
	}

	/**
	 * コピー元のパス[srcPath]から、コピー先のパス[destPath]へ
	 * ファイルのコピーを行います。
	 * コピー処理にはFileChannel#transferToメソッドを利用します。
	 * 尚、コピー処理終了後、入力・出力のチャネルをクローズします。
	 * 
	 * @param srcPath コピー元のパス
	 * @param destPath コピー先のパス
	 * @throws java.io.IOException 何らかの入出力処理例外が発生した場合
	 */
	public static void copyTransfer(String srcPath, String destPath)
			throws IOException {

		FileChannel srcChannel = new FileInputStream(srcPath).getChannel();
		FileChannel destChannel = new FileOutputStream(destPath).getChannel();
		try {
			srcChannel.transferTo(0, srcChannel.size(), destChannel);
		} finally {
			srcChannel.close();
			destChannel.close();
		}
	}

	public static void copyTransfer(File srcFile, File destFile)
			throws IOException {

		FileChannel srcChannel = new FileInputStream(srcFile).getChannel();
		FileChannel destChannel = new FileOutputStream(destFile).getChannel();
		try {
			srcChannel.transferTo(0, srcChannel.size(), destChannel);
		} finally {
			srcChannel.close();
			destChannel.close();
		}
	}

}
