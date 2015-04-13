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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import static net.granoeste.commons.util.LogUtils.LOGD;
import static net.granoeste.commons.util.LogUtils.makeLogTag;

/**
 * ソケットサーバー
 */
public class SocketServer {
    private static final String TAG = makeLogTag(SocketServer.class);

    public static final int DEFAULT_SERVER_PORT = 10007;
    Thread mServerThread;
    ServerSocket mServerSocket;

    boolean mIsAbortReceiverThread = false;

    /**
     * ソケットサーバー起動
     *
     * @param serverPort 接続先ポート
     */
    public void start(int serverPort) {
        mServerThread = new ServerThread(serverPort);
        mServerThread.start();

        mIsAbortReceiverThread = false;
    }

    /**
     * ソケットサーバー起動
     */
    public void start() {
        start(DEFAULT_SERVER_PORT);
    }

    /**
     * ソケットサーバー停止
     */
    public void stop() {
        mIsAbortReceiverThread = true;
        try {
            if (mServerSocket != null) {
                mServerSocket.close();
            }
        } catch (IOException e) {
        }
        mServerThread = null;
    }

    /**
     * ソケット通信サーバースレッド
     */
    class ServerThread extends Thread {
        private int mServerPort;

        public ServerThread(int serverPort) {
            super();
            mServerPort = serverPort;
        }

        @Override
        public void run() {

            try {
                // ソケット接続
                mServerSocket = new ServerSocket(mServerPort);
                LOGD(TAG, "LocalSocketAddress:" + mServerSocket.getLocalSocketAddress());
                LOGD(TAG, "LocalPort         :" + mServerSocket.getLocalPort());

                // スレッドが生きている間はループする。
                while (mServerThread == Thread.currentThread()) {
                    // リクエストの受け取り
                    Socket socket = mServerSocket.accept();
                    // リクエストを別スレッドで処理
                    new ReceiverThread(socket).start();
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (mServerSocket != null) {
                        mServerSocket.close();
                        LOGD(TAG, "ServerSocket is closed.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * リクエスト処理用スレッド
     */
    class ReceiverThread extends Thread {
        /**
         * ソケットサーバーで受け取ったソケット
         */
        private final Socket mSocket;

        /**
         * @param socket ソケット
         */
        public ReceiverThread(Socket socket) {
            this.mSocket = socket;
            LOGD(TAG, "RemoteSocketAddress:" + this.mSocket.getRemoteSocketAddress());
        }

        @Override
        public void run() {
            BufferedReader in = null;
            PrintWriter out = null;
            try {
                // ソケットクライアントからのリクエスト用ライター
                in = new BufferedReader(new InputStreamReader(
                        mSocket.getInputStream()));
                // ソケットクライアントへのレスポンス用リーダー
                out = new PrintWriter(mSocket.getOutputStream(), true);

                // ソケットクライアントからのリクエストを取得し続ける
                String request;
                while ((request = in.readLine()) != null) {
                    // リクエストのコールバック呼び出し
                    String response = mOnRequestListener.onRequest(request);
                    // ソケットクライアントへレスポンスを返す(書き込み)
                    out.println(response);

                    LOGD(TAG, "Request:" + request + " / Response:" + response);
                }

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
                    if (mSocket != null) {
                        mSocket.close();
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
    public interface OnRequestListener {
        public String onRequest(String request);
    }

    private final OnRequestListener mDummyOnRequestListener = new OnRequestListener() {
        @Override
        public String onRequest(String request) {
            return null;
        }

    };

    private OnRequestListener mOnRequestListener = mDummyOnRequestListener;

    public void setOnRequestListener(OnRequestListener onRequestListener) {
        mOnRequestListener = onRequestListener;
    }

}
