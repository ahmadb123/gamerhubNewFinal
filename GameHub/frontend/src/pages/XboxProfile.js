import {getXboxAchievements} from "../service/XboxAchievements";
import {getAllLinkedProfiles} from "../service/searchUserProfile";
function XboxProfile(){

    const getXboxAchievements = async () => {
        try {
            const achievements = await getXboxAchievements();
            console.log("Returned achievements:", achievements);
            this.setState({ achievements });
        } catch (error) {
            console.error(error);
        }
    };
        
    return(
        <div className="xbox-profile">
            {/* Achievements Section */}
            <section className="achievements">
            <h2>Xbox Achievements</h2>
            {achievements.length === 0 ? (
            <p>No achievements found.</p>
            ) : (
            achievements.map((ach, index) => (
                <div key={index} className="achievement-item">
                <p>{ach.description}</p>
                <p>ID: {ach.id}</p>
                {
                    ach.progression && (
                    <div>
                        Time Unlocked: {ach.progression.timeUnlocked}
                    </div>
                    )
                }
        
                {ach.mediaAssets && ach.mediaAssets.length > 0 && (
                    <div className="media-assets">
                    {ach.mediaAssets.map((asset, assetIndex) => (
                        <div key={assetIndex} className="media-asset">
                        <img src={asset.url} alt={asset.name} />
                        <p>{asset.name}</p>
                        </div>
                    ))}
                    </div>
                )}
                </div>
            ))
            )}
        
            {
            ach.titleAssociations && ach.titleAssociations.length > 0 && (
                <div className="title-associations">
                {
                    ach.titleAssociations.map((title , index) => (
                    <div key={index} className="title-association">
                        <p>{title.name}</p>
                        <p>Id: {title.id}</p>
                    </div>
                    ))
                }
                </div>
            )
            }
        </section>
        </div>
    );
}