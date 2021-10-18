package dk.stonemountain.demo.demofx.util.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class IconHelper {
	private static final Logger logger = LoggerFactory.getLogger(IconHelper.class);
	
	public static void patchIconPath(Button btn) {
		logger.debug("Button graphics: {}", btn.getGraphic());
		if (btn.getGraphic() instanceof ImageView) {
			ImageView view = (ImageView) btn.getGraphic();
			logger.debug("Image: {}", view.getImage());
			if (view.getImage() != null) {
				logger.debug("Image Url: {}", view.getImage().getUrl());
				String resourceUrl = "/icons" + view.getImage().getUrl().substring(view.getImage().getUrl().lastIndexOf("/"));
				logger.debug("Now using: {}", resourceUrl);
				btn.setGraphic(new ImageView(IconHelper.class.getResource(resourceUrl).toString()));
			}		
		}
	}
	
	public static ImageView patchIconPath(ImageView v) {
		if (v.getImage() != null) {
			logger.debug("Image Url: {}, {}, {}", v.getImage().getUrl(), v.getFitWidth(), v.getFitHeight());
			String resourceUrl = "/icons" + v.getImage().getUrl().substring(v.getImage().getUrl().lastIndexOf("/"));
			logger.debug("Now using: {}", resourceUrl);
			ImageView v2 = new ImageView(IconHelper.class.getResource(resourceUrl).toString());
			v2.setFitWidth(v.getFitWidth());
			v2.setFitHeight(v.getFitHeight());
			v2.setPreserveRatio(v.isPreserveRatio());
			return v2;
		} else {
			return null;
		}
	}

}
