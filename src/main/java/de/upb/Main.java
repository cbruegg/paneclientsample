package de.upb;

import edu.brown.cs.paneclient.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    private static final String PANE_CONTROLLER_HOSTNAME = "localhost";
    private static final int PANE_CONTROLLER_PORT = 4242;
    private static final String PANE_USERNAME = "username";
    private static final int RESERVATION_BANDWIDTH_BITS_P_S = 1024;
    private static final int FLOW_PROTOCOL = PaneFlowGroup.PROTO_TCP;
    private static final InetAddress FLOW_SRC_HOST;

    private static final int FLOW_SRC_PORT = 80;
    private static final InetAddress FLOW_DST_HOST;
    private static final int FLOW_DST_PORT = 80;
    private static final int MAX_RESERVATIONS = Integer.MAX_VALUE;

    static {
        try {
            FLOW_SRC_HOST = InetAddress.getByName("10.0.0.1");
            FLOW_DST_HOST = InetAddress.getByName("10.0.0.2");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException, PaneException.InvalidResvException, PaneException.InvalidAuthenticateException {
        PaneClient client = new PaneClientImpl(InetAddress.getByName(PANE_CONTROLLER_HOSTNAME), PANE_CONTROLLER_PORT);
        client.authenticate(PANE_USERNAME);

        PaneRelativeTime start = new PaneRelativeTime();
        start.setRelativeTime(500); // Now
        PaneRelativeTime end = new PaneRelativeTime();
        end.setRelativeTime(5000); // In 1000 ms (I think? Unit unclear, but should be ms)

        PaneFlowGroup flowGroup = new PaneFlowGroup();
        flowGroup.setSrcHost(FLOW_SRC_HOST);
        flowGroup.setSrcPort(FLOW_SRC_PORT);
        flowGroup.setDstHost(FLOW_DST_HOST);
        flowGroup.setDstPort(FLOW_DST_PORT);
        flowGroup.setTransportProto(FLOW_PROTOCOL);

        PaneReservation reservation = new PaneReservation(RESERVATION_BANDWIDTH_BITS_P_S, flowGroup, start, end);

        PaneShare share = new PaneShare("sample_share", MAX_RESERVATIONS, flowGroup);
        share.setClient(client);
        share.reserve(reservation);
    }
}
