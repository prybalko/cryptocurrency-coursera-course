package com.company;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.HashSet;

public class TxHandler {

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */

    private UTXOPool pool;

    public TxHandler(UTXOPool utxoPool) {
        pool = utxoPool;
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        HashSet<UTXO> H = new HashSet<UTXO>();

        for (int i=0; i < tx.numInputs(); i++ ) {
            Transaction.Input input = tx.getInput(0);
            UTXO prevUTXO = new UTXO(input.prevTxHash, input.outputIndex);
            // (1) all outputs claimed by {@code tx} are in the current UTXO pool,
            if (!pool.contains(prevUTXO)) {
                return false;
            }

            Transaction.Output prevOutput = pool.getTxOutput(prevUTXO);
            PublicKey pubKey = prevOutput.address;
            // (2) the signatures on each input of {@code tx} are valid,
            if (!Crypto.verifySignature(pubKey, tx.getRawDataToSign(0), tx.getInput(0).signature)) {
                return false;
            }
            // (3) no UTXO is claimed multiple times by {@code tx},
            if (H.contains(prevUTXO)) {
                return false;
            }
            H.add(prevUTXO);

            // (4) all of {@code tx}s output values are non-negative, and
            int outSum = 0;
            for (int j=0; j < tx.numOutputs(); j++ ){
                if (tx.getOutput(j).value < 0) {
                    return false;
                }
                outSum += tx.getOutput(j).value;
            }

            // (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output  values;


        }
        return true;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        // IMPLEMENT THIS
        return possibleTxs;
    }

}
