/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.utils.FakeServerUtils;
import com.google.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class WelcomePageCtrl {
    private final FakeServerUtils fakeServer;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField chosenServer;

    @Inject
    public WelcomePageCtrl(FakeServerUtils fakeServer, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.fakeServer = fakeServer;
    }

    public void connectToChosenServer() {
        // For now, it just sends you to the main board.
        // Eventually it will get the string from chosenServer and change the server host.
        mainCtrl.showBoard();
    }
}