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

package net.granoeste.commons.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static net.granoeste.commons.util.LogUtils.LOGD;
import static net.granoeste.commons.util.LogUtils.makeLogTag;

/**
 * ソケットクライアント
 */
public class SocketClient {
    private static final String TAG = makeLogTag(SocketClient.class);

    public static final int DEFAULT_SERVER_PORT = 10007;
    Thread mClientThread;
    Queue<String> mRequestQueue;

    /**
     * ソケットクライアント起動
     *
     * @param serverHost 接続先ホスト名
     * @param serverPort 接続先ポート
     */
    public void start(String serverHost, int serverPort) {
        mRequestQueue = new LinkedBlockingQueue<String>();
        // ソケット通信を行なうスレッドを起動
        mClientThread = new ClientThread(serverHost, serverPort);
        mClientThread.start();
    }

    /**
     * ソケットクライアント起動
     *
     * @param serverHost 接続先ホスト名
     */
    public void start(String serverHost) {
        start(serverHost, DEFAULT_SERVER_PORT);
    }

    /**
     * ソケットクライアントにリクエストを送る
     *
     * @param request
     */
    public void request(String request) {
        // リクエストキューに追加
        mRequestQueue.add(request);
    }

    /**
     * ソケットクライアント停止
     */
    public void stop() {
//		try {
//			if (mClientSocket != null) {
//				mClientSocket.close();
//			}
//		} catch (IOException e) {
//		}
        mClientThread = null;
    }

    /**
     * ソケット通信クライアントスレッド
     */
    class ClientThread extends Thread {
        /**
         * ホスト名
         */
        private String mServerHost;
        private int mServerPort;

        public ClientThread(String serverHost, int serverPort) {
            super();
            mServerHost = serverHost;
            mServerPort = serverPort;
        }

        @Override
        public void run() {
            Socket socket = null;
            PrintWriter out = null;
            BufferedReader in = null;

            try {
                // ソケット接続
                socket = new Socket();
                socket.connect(new InetSocketAddress(mServerHost, mServerPort));

                LOGD(TAG, "LocalSocketAddress:" + socket.getLocalSocketAddress());
                LOGD(TAG, "LocalPort         :" + socket.getLocalPort());

                // ソケットサーバーへのリクエスト用ライター
                out = new PrintWriter(socket.getOutputStream(), true);
                // ソケットサーバーへのレスポンス用リーダー
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // スレッドが生きている間はループする。
                while (mClientThread == Thread.currentThread()) {
                    // リクエストキューが溜まってる間はループする。
                    while (!mRequestQueue.isEmpty()) {
                        // キューからリクエストを取得
                        String request = mRequestQueue.poll();
                        // ソケットサーバーへリクエストを送る(書き込み)
                        out.println(request);
                        // ソケットサーバーからのレスポンスを受け取る(読み込み)
                        String response = in.readLine();
                        // レスポンスのコールバック呼び出し
                        mOnResponseListener.onResponse(response);

                        LOGD(TAG, "Request:" + request + " / Response:" + response);
                    }
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                    if (socket != null) {
                        socket.close();
                        LOGD(TAG, "Socket is closed.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ------------------------------------------------------------------------
    // OnRequestListener / OnResponseListener
    // ------------------------------------------------------------------------
    public interface OnResponseListener {
        public String onResponse(String response);
    }

    private final OnResponseListener mDummyOnResponseListener = new OnResponseListener() {
        @Override
        public String onResponse(String response) {
            return "";
        }

    };

    private OnResponseListener mOnResponseListener = mDummyOnResponseListener;

    public void setOnResponseListener(OnResponseListener onResponseListener) {
        mOnResponseListener = onResponseListener;
    }
}
