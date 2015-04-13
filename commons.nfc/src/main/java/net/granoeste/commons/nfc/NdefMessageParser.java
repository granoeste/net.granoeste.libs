/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package net.granoeste.commons.nfc;

import java.util.ArrayList;
import java.util.List;

import net.granoeste.commons.nfc.record.ParsedNdefRecord;
import net.granoeste.commons.nfc.record.SmartPoster;
import net.granoeste.commons.nfc.record.TextRecord;
import net.granoeste.commons.nfc.record.UriRecord;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;

/**
 * Utility class for creating {@link ParsedNdefMessage}s.
 */
public class NdefMessageParser {

	// Utility class
	private NdefMessageParser() {

	}

	/** Parse an NdefMessage */
	public static List<ParsedNdefRecord> parse(NdefMessage message) {
		return getRecords(message.getRecords());
	}

	public static List<ParsedNdefRecord> getRecords(NdefRecord[] records) {
		List<ParsedNdefRecord> elements = new ArrayList<ParsedNdefRecord>();
		for (NdefRecord record : records) {
			if (UriRecord.isUri(record)) {
				elements.add(UriRecord.parse(record));
			} else if (TextRecord.isText(record)) {
				elements.add(TextRecord.parse(record));
			} else if (SmartPoster.isPoster(record)) {
				elements.add(SmartPoster.parse(record));
			}
		}
		return elements;
	}

	public static NdefMessage[] getNdefMessages(Intent intent) {
		// Parse the intent
		NdefMessage[] msgs = null;
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			} else {
				// Unknown tag type
				byte[] empty = new byte[] {};
				NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
				NdefMessage msg = new NdefMessage(new NdefRecord[] {
						record
				});
				msgs = new NdefMessage[] {
						msg
				};
			}
		}
		return msgs;
	}

}
