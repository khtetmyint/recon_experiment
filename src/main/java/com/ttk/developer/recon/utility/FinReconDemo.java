package com.ttk.developer.recon.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FinReconDemo {

    /**
     * Reconciliation
     * <ol>
     *     <li>remote Set of upstream orders</li>
     *     <li>local Set of upstream orders</li>
     *     <li>Let's assume that the object in the collection is Map, or, of course, your custom JavaBean, such as OrderInfo or something.</li>
     * </ol>
     */
    static BiFunction<List<Map>, List<Map>, Map> diff = (remote, local) -> {
        /**
         * Convert local orders from List to Map
         * <br>Take order number as key and order information as value
         */
        Map<String,Map> loMaps =
                local.stream().collect(Collectors.toMap((map) -> map.get("orderNo").toString(), map -> map));
        /**
         * Streaming Upstream Data
         */
        Stream<Map> stream = remote.parallelStream();

        /**
         * Reconciliation result
         */
        Map result = new ConcurrentHashMap();
        /**
         * Traversing upstream orders, running reconciliation business
         */
        stream.collect(Collectors.toList()).forEach(reMap->{
            //reMap for upstream orders
            Object orderNo = reMap.get("orederNo");
            //loMap Local Order
            Map loMap = loMaps.get(orderNo);
            /**
             * Come on, take the upstream order [reMap] and the local order [loMap] to compare.
             * This specific process of comparison is reconciliation, comparison of amount, status, time, length of money and so on.
             * Then the results of the accounts are put into storage or landed.
             * You can also deposit the result of the account.
             * If there is a business need, the account results will be responded to by the caller to deal with it separately.
             */
        });
        /**
         * Return the reconciliation results
         */
        return result;
    };

//    public static void main(String[] args) {
//        //Upstream order
//        List<Map> re = new ArrayList();
//        //Local order
//        List<Map> lo = new ArrayList();
//
//        /**
//         * No, let's pretend they have millions of data.
//         */
//
//        System.out.println("Reconciliation begins");
//        Map map = diff.apply(re, lo);
//        System.out.println("Reconciliation completion");
//        System.out.println("Reconciliation result" + map);
//
//    }
}
