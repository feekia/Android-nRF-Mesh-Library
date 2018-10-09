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

package no.nordicsemi.android.nrfmeshprovisioner.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.Map;

import javax.inject.Inject;

import no.nordicsemi.android.meshprovisioner.meshmessagestates.ProvisionedMeshNode;
import no.nordicsemi.android.nrfmeshprovisioner.repository.MeshRepository;
import no.nordicsemi.android.nrfmeshprovisioner.repository.ScannerRepository;

public class SharedViewModel extends ViewModel {

    private final ScannerRepository mScannerRepository;
    private final MeshRepository mMeshRepository;
    private final NrfMeshRepository nRFMeshRepository;

    @Inject
    SharedViewModel(final ScannerRepository scannerRepository, final MeshRepository meshRepository, final NrfMeshRepository nrfMeshRepository, final Context context) {
        mScannerRepository = scannerRepository;
        mMeshRepository = meshRepository;
        nRFMeshRepository = nrfMeshRepository;
        scannerRepository.registerBroadcastReceivers();
        mMeshRepository.registerBroadcastReceiver();
    }

    public NetworkInformationLiveData getNetworkInformation() {
        return nRFMeshRepository.getNetworkInformationLiveData();
    }

    public ProvisioningSettingsLiveData getProvisioningSettingsLiveData() {
        return nRFMeshRepository.getProvisioningSettingsLiveData();
    }

    public LiveData<byte[]> getConfigurationSrc() {
        return nRFMeshRepository.getConfigurationSrcLiveData();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mMeshRepository.disconnect();
        mScannerRepository.unregisterBroadcastReceivers();
        mMeshRepository.unregisterBroadcastReceiver();
        mMeshRepository.stopService();
    }


    /**
     * Returns an instance of the scanner repository
     */
    public ScannerRepository getScannerRepository() {
        return mScannerRepository;
    }

    /**
     * Returns an instance of the mesh repository
     */
    public MeshRepository getMeshRepository() {
        return mMeshRepository;
    }

    /**
     * Returns the provisioned nodes as a live data object.
     */
    public LiveData<Map<Integer, ProvisionedMeshNode>> getProvisionedNodes() {
        return nRFMeshRepository.getProvisionedNodes();
    }

    /**
     * Returns if currently connected to a peripheral device.
     *
     * @return true if connected and false otherwise
     */
    public LiveData<Boolean> isConnected() {
        return nRFMeshRepository.isConnected();
    }

    /**
     * Disconnect from peripheral
     */
    public void disconnect() {
        nRFMeshRepository.disconnect();
    }

    public boolean isConenctedToMesh() {
        return mMeshRepository.isConnectedToMesh();
    }

    /**
     * Returns if currently connected to the mesh network.
     *
     * @return true if connected and false otherwise
     */
    public LiveData<Boolean> isConnectedToNetwork() {
        return nRFMeshRepository.isConnectedToNetwork();
    }

    public void setMeshNode(final ProvisionedMeshNode meshNode) {
        mMeshRepository.setMeshNode(meshNode);
    }

    /**
     * Reset mesh network
     */
    public void resetMeshNetwork() {
        nRFMeshRepository.resetMeshNetwork();
    }

    /**
     * Refresh provisioning data
     */
    public void refreshProvisioningData() {
        mMeshRepository.refreshProvisioningData();
    }

    /**
     * Set the source address to be used for configuration
     *
     * @param srcAddress source address
     * @return true if success
     */
    public boolean setConfiguratorSource(final byte[] srcAddress) {
        return nRFMeshRepository.setConfiguratorSrc(srcAddress);
    }
}
