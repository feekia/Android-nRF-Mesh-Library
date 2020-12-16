/*
 * Copyright (c) 2018, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.nordicsemi.android.nrfmesh.node;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import butterknife.ButterKnife;
import dagger.hilt.android.AndroidEntryPoint;
import no.nordicsemi.android.mesh.Features;
import no.nordicsemi.android.mesh.transport.ProvisionedMeshNode;
import no.nordicsemi.android.mesh.utils.CompanyIdentifiers;
import no.nordicsemi.android.mesh.utils.CompositionDataParser;
import no.nordicsemi.android.mesh.utils.MeshAddress;
import no.nordicsemi.android.mesh.utils.MeshParserUtils;
import no.nordicsemi.android.nrfmesh.R;
import no.nordicsemi.android.nrfmesh.viewmodels.NodeDetailsViewModel;

@AndroidEntryPoint
public class NodeDetailsActivity extends AppCompatActivity {

    NodeDetailsViewModel viewModel;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_details);
        viewModel = new ViewModelProvider(this).get(NodeDetailsViewModel.class);
        ButterKnife.bind(this);

        if (viewModel.getSelectedMeshNode().getValue() == null) {
            finish();
        }

        final ProvisionedMeshNode node = viewModel.getSelectedMeshNode().getValue();
        final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(node.getNodeName());

        final View containerProvisioningTimeStamp = findViewById(R.id.container_timestamp);
        containerProvisioningTimeStamp.setClickable(false);
        final TextView timestamp = containerProvisioningTimeStamp.findViewById(R.id.text);
        final String format = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(node.getTimeStamp());
        timestamp.setText(format);

        final View containerUnicastAddress = findViewById(R.id.container_supported_algorithm);
        containerUnicastAddress.setClickable(false);
        final TextView unicastAddress = containerUnicastAddress.findViewById(R.id.text);
        unicastAddress.setText(MeshParserUtils.bytesToHex(MeshAddress.addressIntToBytes(node.getUnicastAddress()), false));

        final View containerDeviceKey = findViewById(R.id.container_device_key);
        containerDeviceKey.setClickable(false);
        final TextView deviceKey = containerDeviceKey.findViewById(R.id.text);
        deviceKey.setText(MeshParserUtils.bytesToHex(node.getDeviceKey(), false));

        final View copyDeviceKey = findViewById(R.id.copy);
        copyDeviceKey.setOnClickListener(v -> {
            if (clipboard != null) {
                final ClipData clipDeviceKey = ClipData.newPlainText("Device Key", MeshParserUtils.bytesToHex(node.getDeviceKey(), false));
                clipboard.setPrimaryClip(clipDeviceKey);
                Toast.makeText(NodeDetailsActivity.this, R.string.device_key_clipboard_copied, Toast.LENGTH_SHORT).show();
            }
        });

        final View containerCompanyIdentifier = findViewById(R.id.container_company_identifier);
        containerCompanyIdentifier.setClickable(false);
        final TextView companyIdentifier = containerCompanyIdentifier.findViewById(R.id.text);
        if (node.getCompanyIdentifier() != null) {
            companyIdentifier.setText(CompanyIdentifiers.getCompanyName(node.getCompanyIdentifier().shortValue()));
        } else {
            companyIdentifier.setText(R.string.unknown);
        }

        final View containerProductIdentifier = findViewById(R.id.container_product_identifier);
        containerProductIdentifier.setClickable(false);
        final TextView productIdentifier = containerProductIdentifier.findViewById(R.id.text);
        if (node.getProductIdentifier() != null) {
            productIdentifier.setText(CompositionDataParser.formatProductIdentifier(node.getProductIdentifier().shortValue(), false));
        } else {
            productIdentifier.setText(R.string.unavailable);
        }

        final View containerProductVersion = findViewById(R.id.container_product_version);
        containerProductVersion.setClickable(false);
        final TextView productVersion = containerProductVersion.findViewById(R.id.text);
        if (node.getVersionIdentifier() != null) {
            productVersion.setText(CompositionDataParser.formatVersionIdentifier(node.getVersionIdentifier().shortValue(), false));
        } else {
            productVersion.setText(R.string.unavailable);
        }

        final View containerCrpl = findViewById(R.id.container_crpl);
        containerCrpl.setClickable(false);
        final TextView crpl = containerCrpl.findViewById(R.id.text);
        if (node.getCrpl() != null) {
            crpl.setText(CompositionDataParser.formatReplayProtectionCount(node.getCrpl().shortValue(), false));
        } else {
            crpl.setText(R.string.unavailable);
        }

        final View containerFeatures = findViewById(R.id.container_features);
        containerFeatures.setClickable(false);
        final TextView features = containerFeatures.findViewById(R.id.text);
        if (node.getNodeFeatures() != null) {
            features.setText(parseFeatures(node.getNodeFeatures()));
        } else {
            features.setText(R.string.unavailable);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    /**
     * Returns a String representation of the features
     */
    private String parseFeatures(final Features features) {
        return "Relay feature: " +
                (features.isRelayFeatureSupported() ? isEnabled(features.getRelay()) : "Unsupported") +
                "\nProxy feature: " +
                (features.isProxyFeatureSupported() ? isEnabled(features.getProxy()) : "Unsupported") +
                "\nFriend feature: " +
                (features.isFriendFeatureSupported() ? isEnabled(features.getFriend()) : "Unsupported") +
                "\nLow power feature: " +
                (features.isLowPowerFeatureSupported() ? isEnabled(features.getLowPower()) : "Unsupported");
    }

    public String isEnabled(final int feature) {
        return feature == Features.ENABLED ? "Enabled" : "Disabled";
    }
}
